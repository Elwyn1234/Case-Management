package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import elwyn.case_management.models.Contact;
import elwyn.case_management.models.ContactMethod;

public class ContactController extends RecordController<Contact> {
  CaseController caseController;
  protected String tableName() { return "contacts"; }

  public ContactController(CaseController caseController) {
    super();
    this.caseController = caseController;
  } // eTODO: this can probably be replaced with RecordController

  public List<Contact> readRecords() {
    ArrayList<Contact> contacts = new ArrayList<Contact>();
    try {
      String sql = "SELECT rowid, * from contacts"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        Contact record = new Contact();
        long caseId = rs.getLong("caseId");
        record.caseRecord = caseController.readRecord(caseId);

        record.id = rs.getLong("rowid");
        record.description = rs.getString("description");
        record.date = rs.getString("date");
        record.time = rs.getString("time");
        record.contactMethod = ContactMethod.parseSelectedContactMethod(rs.getString("contactMethod"));
        contacts.add(record);
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return contacts;
  }

  protected void recursiveDelete(long rowid) {} // eTODO: how does this work, does sql cascade on delete handle this

  protected PreparedStatement buildInsertPreparedStatement(Contact record) throws SQLException {
    String sql="INSERT INTO contacts " + 
        "(description, date, time, contactMethod, caseId) " +
        "VALUES (?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Contact record) throws SQLException {
    String sql="UPDATE contacts SET " +
        "description=?, date=?, time=?, contactMethod=?, caseId=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(6, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, Contact record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.description);
    pStatement.setString(2, record.date);
    pStatement.setString(3, record.time);
    pStatement.setString(4, record.contactMethod == null ? null : record.contactMethod.toString());
    pStatement.setLong(5, record.caseRecord.id);
    return pStatement;
  }
    
  public boolean isRecordValid(Contact record) {
    if (record.description.length() <= 0)
      return false;
    if (record.date.length() <= 0)
      return false;
    if (record.time.length() <= 0)
      return false;
    if (record.contactMethod == null)
      return false;
    if (record.caseRecord == null)
      return false;
    return true;
  }
}
