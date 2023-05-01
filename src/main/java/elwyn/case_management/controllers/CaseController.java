package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.swing.JComponent;

import elwyn.case_management.models.Case;
import elwyn.case_management.models.Log;
import elwyn.case_management.models.Priority;
import elwyn.case_management.models.Severity;
import elwyn.case_management.models.User;

public class CaseController extends RecordController<Case> {
  public CustomerController customerController;
  public UserController userController;
  Function<List<Case>, List<Case>> filter;
  protected String tableName() { return "cases"; }
  protected String recordName() { return "Case"; }
  protected Boolean logMe() { return true; }

  public CaseController(User loggedInUser, Function<List<Case>, List<Case>> filter) {
    super(loggedInUser, new LogController(loggedInUser));
    this.customerController = new CustomerController(loggedInUser);
    this.userController = new UserController(loggedInUser);
    this.filter = filter;
  }

  public List<Case> readRecords(int page) {
    ArrayList<Case> cases = new ArrayList<Case>();
    try {
      String sql = "SELECT rowid, * FROM cases ORDER BY yearOpened DESC, monthOpened DESC, dayOpened DESC LIMIT ?,?"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setInt(1, page * PAGE_SIZE);
      pStatement.setInt(2, PAGE_SIZE);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        cases.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    if (filter != null)
      return filter.apply(cases);
    else
      return cases;
  }

  public Case readRecord(long rowid) {
    Case caseRecord = new Case();
    try {
      String sql = "SELECT rowid, * FROM cases WHERE rowid=?"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();
            
      int count = 0;
      while (rs.next()) {
        count++;
        caseRecord = readResultSet(rs);
      }
      if (count == 0)
        caseRecord = null; // No record found
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return caseRecord;
  }

  public Case readResultSet(ResultSet rs) throws SQLException {
    Case record = new Case();
    long customerId = rs.getLong("customer");
    record.customer = customerController.readRecord(customerId);
    long createdById = rs.getLong("createdBy");
    record.createdBy = userController.readRecord(createdById);
    long assignedToId = rs.getLong("assignedTo");
    if (!rs.wasNull()) {
      record.assignedTo = userController.readRecord(assignedToId);
    }

    record.id = rs.getLong("rowid");
    record.summary = rs.getString("summary");
    record.description = rs.getString("description");
    int dayOpened = rs.getInt("dayOpened");
    int monthOpened = rs.getInt("monthOpened");
    int yearOpened = rs.getInt("yearOpened");
    int secondOpened = rs.getInt("secondOpened");
    int minuteOpened = rs.getInt("minuteOpened");
    int hourOpened = rs.getInt("hourOpened");
    record.dateOpened = new Date(yearOpened, monthOpened, dayOpened, hourOpened, minuteOpened, secondOpened);
    int dayClosed = rs.getInt("dayClosed"); // eTODO: somehow check for null
    int monthClosed = rs.getInt("monthClosed");
    int yearClosed = rs.getInt("yearClosed");
    int secondClosed = rs.getInt("secondClosed");
    int minuteClosed = rs.getInt("minuteClosed");
    int hourClosed = rs.getInt("hourClosed");
    if (!rs.wasNull()) {
      record.dateClosed = new Date(yearClosed, monthClosed, dayClosed, hourClosed, minuteClosed, secondClosed);
    }
    record.priority = Priority.parseSelectedPriority(rs.getString("priority"));
    return record;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Case record) throws SQLException {
  // public List<Contact> contacts
    String sql="INSERT INTO cases " + 
        "(summary, " +
        "description, " +
        "customer, " +
        "assignedTo, " +
        "priority, " +
        "createdBy, " +
        "dayOpened, " +
        "monthOpened, " +
        "yearOpened, " +
        "secondOpened, " +
        "minuteOpened, " +
        "hourOpened) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    record.dateOpened = new Date();
    pStatement.setLong(6, loggedInUser.id);
    pStatement.setInt(7, record.dateOpened.getDate());
    pStatement.setInt(8, record.dateOpened.getMonth());
    pStatement.setInt(9, record.dateOpened.getYear());
    pStatement.setInt(10, record.dateOpened.getSeconds());
    pStatement.setInt(11, record.dateOpened.getMinutes());
    pStatement.setInt(12, record.dateOpened.getHours());

    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Case record) throws SQLException {
    String sql="UPDATE cases SET " +
        "summary=?, " +
        "description=?, " +
        "customer=?, " +
        "assignedTo=?, " +
        "priority=? " +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(6, record.id);

    return pStatement;
  }

  private PreparedStatement PopulateCommonSqlParameters(String sql, Case record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.summary);
    pStatement.setString(2, record.description);
    pStatement.setLong(3, record.customer.id);
    pStatement.setLong(4, record.assignedTo.id);
    pStatement.setString(5, record.priority == null ? null : record.priority.toString());
    return pStatement;
  }
   
  public JComponent closeRecord(Long rowid) {
    String sql="UPDATE cases SET " +
        "dayClosed=?, " +
        "monthClosed=?, " +
        "yearClosed=?, " +
        "secondClosed=?, " +
        "minuteClosed=?, " +
        "hourClosed=? " +
        "WHERE rowid=?";
    try {
      PreparedStatement pStatement = conn.prepareStatement(sql);
      Date dateClosed = new Date();
      pStatement.setInt(1, dateClosed.getDate());
      pStatement.setInt(2, dateClosed.getMonth());
      pStatement.setInt(3, dateClosed.getYear());
      pStatement.setInt(4, dateClosed.getSeconds());
      pStatement.setInt(5, dateClosed.getMinutes());
      pStatement.setInt(6, dateClosed.getHours());
      pStatement.setLong(7, rowid);
      pStatement.executeUpdate();

      Log log = new Log();
      log.severity = Severity.LOG;
      log.user = loggedInUser.id;
      log.log = "Case Closed: " + rowid;
      logController.createRecord(log);

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  //*********** Component Initialisation ***********//

  public Boolean shouldShowButton(Case record) {
    return !record.getStatus().startsWith("Closed");
  }

  public static boolean caseClosed(Case record) {
    if (record.dateClosed == null)
      return false;
    else
      return true;
  }
}
