package elwyn.case_management.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import elwyn.case_management.models.Log;
import elwyn.case_management.models.Record;
import elwyn.case_management.models.Severity;
import elwyn.case_management.models.User;

public abstract class RecordController <T extends Record> {
  public List<T> records; // eTODO: is this ever getting set / used
  Connection conn;
  static final int PAGE_SIZE = 20;
  public User loggedInUser;
  LogController logController;

  protected abstract String tableName();
  protected abstract String recordName();
  protected abstract Boolean logMe();

  public RecordController(User loggedInUser, LogController logController) {
    this.loggedInUser = loggedInUser;
    this.logController = logController;

    try { // eTODO: dependency injection
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:assets/caseManagement.db");
      if (conn == null) {
        System.out.println("ERROR: Your Database probably cant be found");
      }
    } catch (Exception ex) {
      // eTODO: log
      // Logger.getLogger(UserManagementView.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("ERROR: Your Database probably cant be found");
    }
  }

  public void createRecord(T record) {
    createOrUpdateRecord(record, true);
  }

  public abstract List<T> readRecords(int page);
  public abstract T readRecord(long rowid);

  public void updateRecord(T record) {
    createOrUpdateRecord(record, false);
  }

  public void deleteRecord(long recordId) {
    try {
      String sql = "DELETE FROM " + tableName() + " where rowid=?;";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, recordId);
      pStatement.executeUpdate(); // eTODO: Do we need to handle the return value
      recursiveDelete(recordId);

      if (logController != null && logMe()) {
        Log log = new Log();
        log.severity = Severity.DEBUG;
        log.user = loggedInUser.id;
        log.log = recordName() + " Deleted: " + recordId;
        logController.createRecord(log);
      }

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
  protected abstract void recursiveDelete(long rowid);

  public int rowCount() { // eTODO: this should consider a filter
    int rowCount = 0;
    try {
      String sql = "SELECT COUNT(1) FROM " + tableName() + ";";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      ResultSet rs = pStatement.executeQuery(); // eTODO: Do we need to handle the return value
      while (rs.next()) {
        rowCount = rs.getInt(1);
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return rowCount;
  }

  public int pageCount() {
    return (rowCount() + PAGE_SIZE - 1) / PAGE_SIZE;
  }


  //*********** Utilities ***********//

  private void createOrUpdateRecord(T record, boolean createMode) {
    try {
      PreparedStatement pStatement = createMode ? buildInsertPreparedStatement(record) : buildUpdatePreparedStatement(record);
      pStatement.executeUpdate();

      if (logController != null && logMe()) {
        String logVerb = " Updated ";
        if (createMode)
          logVerb = " Created ";
        Log log = new Log();
        log.severity = Severity.DEBUG;
        log.user = loggedInUser.id;
        log.log = recordName() + logVerb + record.toString(0);
        logController.createRecord(log);
      }

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
  }
  protected abstract PreparedStatement buildUpdatePreparedStatement(T record) throws SQLException;
  protected abstract PreparedStatement buildInsertPreparedStatement(T record) throws SQLException;
}

