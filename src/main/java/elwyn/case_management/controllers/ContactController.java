package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import elwyn.case_management.models.Case;
import elwyn.case_management.models.Contact;
import elwyn.case_management.models.ContactMethod;

public class ContactController extends RecordController<Contact> {
  CaseController caseController;
  UserController userController;
  protected String tableName() { return "contacts"; }

  public ContactController(CaseController caseController, UserController userController) {
    super();
    this.caseController = caseController;
    this.userController = userController;
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
        long userId = rs.getLong("user");
        record.user = userController.readRecord(userId);

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
        "(description, date, time, contactMethod, caseId, user) " +
        "VALUES (?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Contact record) throws SQLException {
    String sql="UPDATE contacts SET " +
        "description=?, date=?, time=?, contactMethod=?, caseId=?, user=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(7, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, Contact record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.description);
    pStatement.setString(2, record.date);
    pStatement.setString(3, record.time);
    pStatement.setString(4, record.contactMethod == null ? null : record.contactMethod.toString());
    pStatement.setLong(5, record.caseRecord.id);
    pStatement.setLong(6, record.user.id);
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
    if (record.user == null)
      return false;
    return true;
  }

  public static List<Contact> selectMyContacts(List<Contact> contacts) {
    List<Contact> filteredContacts = new ArrayList<Contact>();
    for (Contact contactRecord : contacts) {
      if (contactRecord.user.id == 1)
        filteredContacts.add(contactRecord);
    }
    return filteredContacts;
  }
}
