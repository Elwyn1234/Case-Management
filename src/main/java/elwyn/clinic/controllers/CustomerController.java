package elwyn.clinic.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;

import elwyn.clinic.models.Customer;
import elwyn.clinic.models.Gender;
import elwyn.clinic.models.Log;
import elwyn.clinic.models.Severity;
import elwyn.clinic.models.User;

public class CustomerController extends RecordController<Customer> {
  protected String tableName() { return "customers"; }
  protected String recordName() { return "Customer"; }
  protected Boolean logMe() { return true; }
  protected UserController userController;

  public CustomerController(User loggedInUser) {
    super(loggedInUser, new LogController(loggedInUser));
    this.userController = new UserController(loggedInUser);
  }

  public List<Customer> readRecords(int page) {
    ArrayList<Customer> customers = new ArrayList<Customer>();
    try {
      String sql = "SELECT rowid, * FROM customers ORDER BY firstName ASC LIMIT ?,?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setInt(1, page * PAGE_SIZE);
      pStatement.setInt(2, PAGE_SIZE);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        customers.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return customers;
  }

  public Customer readRecord(long rowid) {
    Customer customer = new Customer();
    try {
      String sql = "SELECT rowid, * FROM customers WHERE rowid=?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();
            
      int count = 0;
      while (rs.next()) {
        count++;
        customer = readResultSet(rs);
      }
      if (count == 0)
        customer = null; // No record found
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return customer;
  }

  public List<String> readFamilies() {
    ArrayList<String> families = new ArrayList<String>();
    try {
      String sql = "SELECT DISTINCT familyName FROM customers WHERE familyName!='' AND familyName IS NOT NULL ORDER BY familyName ASC";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        families.add(rs.getString("familyName"));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return families;
  }

  public Customer readResultSet(ResultSet rs) throws SQLException {
    Customer customer = new Customer();
    customer.id = rs.getLong("rowid");
    customer.firstName = rs.getString("firstName");
    customer.secondName = rs.getString("secondName");
    customer.sirname = rs.getString("sirname");
    customer.gender = Gender.parseSelectedGender(rs.getString("gender"));
    int day = rs.getInt("dayOfBirth");
    int month = rs.getInt("monthOfBirth");
    int year = rs.getInt("yearOfBirth") - 1900;
    customer.dateOfBirth = new Date(year, month, day);
    customer.otherNotes = rs.getString("otherNotes");
    customer.email = rs.getString("email");
    customer.phoneNumber = rs.getString("phoneNumber");
    customer.address = rs.getString("address");
    customer.city = rs.getString("city");
    customer.postcode = rs.getString("postcode");
    customer.country = rs.getString("country");
    customer.active = rs.getString("active") == "true" ? true : false;
    customer.familyName = rs.getString("familyName");

    customer.healthConditions = rs.getString("healthConditions");
    customer.currentPrescriptions = rs.getString("currentPrescriptions");
    customer.allergies = rs.getString("allergies");
    return customer;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Customer record) throws SQLException {
    String sql="INSERT INTO customers " + 
        "(firstName, " +
        "secondName, " +
        "sirname, " +
        "dayOfBirth, " +
        "monthOfBirth, " +
        "yearOfBirth, " +
        "otherNotes, " +
        "email, " +
        "phoneNumber, " +
        "address, " +
        "city, " +
        "postcode, " +
        "country, " +
        "gender, " +
        "familyName, " +
        "healthConditions, " +
        "currentPrescriptions, " +
        "allergies, " +
        "active) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setString(19, "true");
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Customer record) throws SQLException {
    String sql="UPDATE customers SET " +
        "firstName=?, " +
        "secondName=?, " +
        "sirname=?, " +
        "dayOfBirth=?, " +
        "monthOfBirth=?, " +
        "yearOfBirth=?, " +
        "otherNotes=?, " +
        "email=?, " +
        "phoneNumber=?, " +
        "address=?, " +
        "city=?, " +
        "postcode=?, " +
        "country=?, " +
        "gender=?, " +
        "familyName=?, " +
        "healthConditions=?, " +
        "currentPrescriptions=?, " +
        "allergies=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(19, record.id);
    return pStatement;
  }

  private PreparedStatement PopulateCommonSqlParameters(String sql, Customer record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.firstName);
    pStatement.setString(2, record.secondName);
    pStatement.setString(3, record.sirname);
    pStatement.setInt(4, record.dateOfBirth.getDate());
    pStatement.setInt(5, record.dateOfBirth.getMonth());
    pStatement.setInt(6, record.dateOfBirth.getYear() + 1900);
    pStatement.setString(7, record.otherNotes);
    pStatement.setString(8, record.email);
    pStatement.setString(9, record.phoneNumber);
    pStatement.setString(10, record.address);
    pStatement.setString(11, record.city);
    pStatement.setString(12, record.postcode);
    pStatement.setString(13, record.country);
    pStatement.setString(14, record.gender == null ? null : record.gender.toString());
    pStatement.setString(15, record.familyName);
    pStatement.setString(16, record.healthConditions);
    pStatement.setString(17, record.currentPrescriptions);
    pStatement.setString(18, record.allergies);
    return pStatement;
  }

  public JComponent deactivateAccount(Long rowid) {
    try {
      String sql="UPDATE customers SET " +
          "active=? " +
          "WHERE rowid=?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setString(1, "false");
      pStatement.setLong(2, rowid);
      pStatement.executeUpdate();

      Log log = new Log();
      log.severity = Severity.LOG;
      log.user = loggedInUser.id;
      log.log = "Patient account deactivated: " + rowid;
      logController.createRecord(log);
    }
    catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }

    return null;
  }

  public Boolean shouldShowButton(Customer record) {
    return record.active;
  }
}
