package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import elwyn.case_management.models.Case;
import elwyn.case_management.models.Priority;

public class CaseController extends RecordController<Case> {
  CustomerController customerController;
  UserController userController;
  Function<List<Case>, List<Case>> filter;
  protected String tableName() { return "cases"; }

  public CaseController(Function<List<Case>, List<Case>> filter) {
    super();
    this.customerController = new CustomerController();
    this.userController = new UserController();
    this.filter = filter;
  }

  public List<Case> readRecords() {
    ArrayList<Case> cases = new ArrayList<Case>();
    try {
      String sql = "SELECT rowid, * from cases"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
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
            
      while (rs.next()) {
        caseRecord = readResultSet(rs);
      }
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
    long userId = rs.getLong("user");
    record.user = userController.readRecord(userId);

    record.id = rs.getLong("rowid");
    record.summary = rs.getString("summary");
    record.description = rs.getString("description");
    record.dateOpened = rs.getString("dateOpened");
    record.dateClosed = rs.getString("dateClosed");
    record.priority = Priority.parseSelectedPriority(rs.getString("priority"));
    return record;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Case record) throws SQLException {
  // public List<Contact> contacts
    String sql="INSERT INTO cases " + 
        "(summary, description, customer, user, dateOpened, dateClosed, priority) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Case record) throws SQLException {
    String sql="UPDATE cases SET " +
        "summary=?, description=?, customer=?, user=?, dateOpened=?, dateClosed=?, priority=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(8, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, Case record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.summary);
    pStatement.setString(2, record.description);
    pStatement.setLong(3, record.customer.id);
    pStatement.setLong(4, record.user.id);
    pStatement.setString(5, record.dateOpened);
    pStatement.setString(6, record.dateClosed);
    pStatement.setString(7, record.priority == null ? null : record.priority.toString());
    return pStatement;
  }
    
  public boolean isRecordValid(Case record) {
    if (record.summary.length() <= 0)
      return false;
    if (record.customer == null)
      return false;
    if (record.user == null)
      return false;
    if (record.dateOpened.length() <= 0)
      return false;
    if (record.priority == null)
      return false;
    return true;
  }

  public static boolean caseClosed(Case record) {
    if (record.dateClosed == null || record.dateClosed.equals(""))
      return false;
    else
      return true;
  }

  public static List<Case> selectMyCases(List<Case> cases) {
    List<Case> filteredCases = new ArrayList<Case>();
    for (Case caseRecord : cases) {
      if (caseRecord.user.id == 1)
        filteredCases.add(caseRecord);
    }
    return filteredCases;
  }
}
