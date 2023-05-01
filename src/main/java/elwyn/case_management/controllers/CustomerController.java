package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import elwyn.case_management.models.Customer;
import elwyn.case_management.models.Gender;
import elwyn.case_management.models.User;

public class CustomerController extends RecordController<Customer> {
  protected String tableName() { return "customers"; }
  protected String recordName() { return "Customer"; }
  protected Boolean logMe() { return true; }

  public CustomerController(User loggedInUser) {
    super(loggedInUser, new LogController(loggedInUser));
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
    return customer;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Customer record) throws SQLException {
    String sql="INSERT INTO customers " + 
        "(firstName, secondName, sirname, dayOfBirth, monthOfBirth, yearOfBirth, otherNotes, email, phoneNumber, address, city, postcode, country, gender) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Customer record) throws SQLException {
    String sql="UPDATE customers SET " +
        "firstName=?, secondName=?, sirname=?, dayOfBirth=?, monthOfBirth=?, yearOfBirth=?, otherNotes=?, email=?, phoneNumber=?, address=?, city=?, postcode=?, country=?, gender=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(15, record.id);
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
    return pStatement;
  }
}
