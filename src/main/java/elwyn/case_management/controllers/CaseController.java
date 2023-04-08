package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import elwyn.case_management.models.Case;
import elwyn.case_management.models.Priority;

public class CaseController extends RecordController<Case> {
  CustomerController customerController;
  protected String tableName() { return "cases"; }

  public CaseController(CustomerController customerController) {
    super();
    this.customerController = customerController;
  }

  public List<Case> readRecords() {
    ArrayList<Case> cases = new ArrayList<Case>();
    try {
      String sql = "SELECT rowid, * from cases"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        Case record = new Case();
        long customerId = rs.getLong("customer");
        record.customer = customerController.readRecord(customerId);

        record.id = rs.getLong("rowid");
        record.summary = rs.getString("summary");
        record.description = rs.getString("description");
        record.dateOpened = rs.getString("dateOpened");
        record.dateClosed = rs.getString("dateClosed");
        record.priority = Priority.parseSelectedPriority(rs.getString("priority"));
        cases.add(record);
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return cases;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Case record) throws SQLException {
  // public List<Contact> contacts
    String sql="INSERT INTO cases " + 
        "(summary, description, customer, dateOpened, dateClosed, priority) " +
        "VALUES (?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Case record) throws SQLException {
    String sql="UPDATE cases SET " +
        "summary=?, description=?, customer=?, dateOpened=?, dateClosed=?, priority=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(7, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, Case record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.summary);
    pStatement.setString(2, record.description);
    pStatement.setLong(3, record.customer.id);
    pStatement.setString(4, record.dateOpened);
    pStatement.setString(5, record.dateClosed);
    pStatement.setString(6, record.priority == null ? null : record.priority.toString());
    return pStatement;
  }
    
  public boolean isRecordValid(Case record) {
    if (record.summary.length() <= 0)
      return false;
    if (record.customer == null)
      return false;
    if (record.dateOpened.length() <= 0)
      return false;
    if (record.priority == null)
      return false;
    return true;
  }
}
