package elwyn.case_management.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import elwyn.case_management.models.Record;

public abstract class RecordController <T extends Record> {
  public List<T> records; // eTODO: is this ever getting set / used
  Connection conn;

  protected abstract String tableName();

  public RecordController() {
    try { // eTODO: dependency injection
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:assets/caseManagement.db");
    } catch (Exception ex) {
      // eTODO: log
      // Logger.getLogger(UserManagementView.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void createRecord(T record) {
    createOrUpdateRecord(record, true);
  }

  public abstract List<T> readRecords();

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
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
  protected abstract void recursiveDelete(long rowid);



  //*********** Utilities ***********//

  public abstract boolean isRecordValid(T record);

  private void createOrUpdateRecord(T record, boolean createMode) {
    try {
      PreparedStatement pStatement = createMode ? buildInsertPreparedStatement(record) : buildUpdatePreparedStatement(record);
      pStatement.executeUpdate();
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
  }
  protected abstract PreparedStatement buildUpdatePreparedStatement(T record) throws SQLException;
  protected abstract PreparedStatement buildInsertPreparedStatement(T record) throws SQLException;
}

