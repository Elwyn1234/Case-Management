package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
  protected String recordName() { return "Contact"; }
  protected Boolean logMe() { return true; }

  public ContactController(User loggedInUser, Function<List<Contact>, List<Contact>> filter) {
    super(loggedInUser, new LogController(loggedInUser));
    this.caseController = new CaseController(loggedInUser, null);
    this.userController = new UserController(loggedInUser);
    this.filter = filter;
  }

  public List<Contact> readRecords(int page) {
    ArrayList<Contact> contacts = new ArrayList<Contact>();
    try {
      String sql = "SELECT rowid, * from contacts ORDER BY year DESC, month DESC, day DESC LIMIT ?,?"; // eTODO: Use tableName function
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

        int day = rs.getInt("day");
        int month = rs.getInt("month");
        int year = rs.getInt("year");
        int second = rs.getInt("second");
        int minute = rs.getInt("minute");
        int hour = rs.getInt("hour");
        record.date = new Date(year, month, day, hour, minute, second);

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
      String sql = "SELECT day, month, year, second, minute, hour, contactMethod FROM contacts WHERE user=? ORDER BY year DESC, month DESC, day DESC"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, userId);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        Contact record = new Contact();
        record.user = new User();
        record.user.id = userId;
        record.contactMethod = ContactMethod.parseSelectedContactMethod(rs.getString("contactMethod"));

        int day = rs.getInt("day");
        int month = rs.getInt("month");
        int year = rs.getInt("year");
        int second = rs.getInt("second");
        int minute = rs.getInt("minute");
        int hour = rs.getInt("hour");
        record.date = new Date(year, month, day, hour, minute, second);

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
        "(description, contactMethod, caseId, user, day, month, year, second, minute, hour) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    record.date = new Date();
    pStatement.setLong(5, record.date.getDate());
    pStatement.setLong(6, record.date.getMonth());
    pStatement.setLong(7, record.date.getYear());
    pStatement.setLong(8, record.date.getSeconds());
    pStatement.setLong(9, record.date.getMinutes());
    pStatement.setLong(10, record.date.getHours());
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Contact record) throws SQLException {
    String sql="UPDATE contacts SET " +
        "description=?, contactMethod=?, caseId=?, user=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(5, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, Contact record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.description);
    pStatement.setString(2, record.contactMethod == null ? null : record.contactMethod.toString());
    pStatement.setLong(3, record.caseRecord.id);
    pStatement.setLong(4, record.user.id);
    return pStatement;
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
