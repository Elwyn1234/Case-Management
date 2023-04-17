package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import elwyn.case_management.models.Contact;
import elwyn.case_management.models.ContactMethod;
import elwyn.case_management.models.User;

public class ContactController extends RecordController<Contact> {
  CaseController caseController;
  UserController userController;
  Function<List<Contact>, List<Contact>> filter;
  protected String tableName() { return "contacts"; }

  public ContactController(Function<List<Contact>, List<Contact>> filter) {
    super();
    this.caseController = new CaseController(null);
    this.userController = new UserController();
    this.filter = filter;
  }

  public List<Contact> readRecords(int page) {
    ArrayList<Contact> contacts = new ArrayList<Contact>();
    try {
      String sql = "SELECT rowid, * from contacts ORDER BY date DESC LIMIT ?,?"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setInt(1, page * PAGE_SIZE);
      pStatement.setInt(2, PAGE_SIZE);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        Contact record = new Contact();
        long caseId = rs.getLong("caseId");
        record.caseRecord = caseController.readRecord(caseId);
        long userId = rs.getLong("user");
        record.user = userController.readRecord(userId);

        record.id = rs.getLong("rowid");
        record.description = rs.getString("description");

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        record.date = df.parse(rs.getString("date"));

        record.time = rs.getString("time");
        record.contactMethod = ContactMethod.parseSelectedContactMethod(rs.getString("contactMethod"));
        contacts.add(record);
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    if (filter != null)
      return filter.apply(contacts);
    else
      return contacts;
  }

  public List<Contact> readRecords(long userId) {
    ArrayList<Contact> contacts = new ArrayList<Contact>();
    try {
      String sql = "SELECT date, contactMethod FROM contacts WHERE user=?"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, userId);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        Contact record = new Contact();
        record.user = new User();
        record.user.id = userId;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        record.date = df.parse(rs.getString("date"));
        record.contactMethod = ContactMethod.parseSelectedContactMethod(rs.getString("contactMethod"));
        contacts.add(record);
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    if (filter != null)
      return filter.apply(contacts);
    else
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
    pStatement.setString(2, record.date.toString());
    pStatement.setString(3, record.time);
    pStatement.setString(4, record.contactMethod == null ? null : record.contactMethod.toString());
    pStatement.setLong(5, record.caseRecord.id);
    pStatement.setLong(6, record.user.id);
    return pStatement;
  }
    
  public boolean isRecordValid(Contact record) {
    if (record.description.length() <= 0)
      return false;
    if (record.date == null)
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
