package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import elwyn.case_management.models.Customer;
import elwyn.case_management.models.Gender;

public class CustomerController extends RecordController<Customer> {
  protected String tableName() { return "customers"; }

  public List<Customer> readRecords() {
    ArrayList<Customer> customers = new ArrayList<Customer>();
    try {
      String sql = "SELECT rowid, * from customers";
      PreparedStatement pStatement = conn.prepareStatement(sql);
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
            
      while (rs.next()) {
        customer = readResultSet(rs);
      }
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
    customer.dateOfBirth = rs.getString("dateOfBirth");
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
        "(firstName, secondName, sirname, dateOfBirth, otherNotes, email, phoneNumber, address, city, postcode, country, gender) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Customer record) throws SQLException {
    String sql="UPDATE customers SET " +
        "firstName=?, secondName=?, sirname=?, dateOfBirth=?, otherNotes=?, email=?, phoneNumber=?, address=?, city=?, postcode=?, country=?, gender=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(13, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, Customer record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.firstName);
    pStatement.setString(2, record.secondName);
    pStatement.setString(3, record.sirname);
    pStatement.setString(4, record.dateOfBirth);
    pStatement.setString(5, record.otherNotes);
    pStatement.setString(6, record.email);
    pStatement.setString(7, record.phoneNumber);
    pStatement.setString(8, record.address);
    pStatement.setString(9, record.city);
    pStatement.setString(10, record.postcode);
    pStatement.setString(11, record.country);
    pStatement.setString(12, record.gender == null ? null : record.gender.toString());
    return pStatement;
  }
    
  public boolean isRecordValid(Customer record) {
    if (record.firstName.length() <= 0)
      return false;
    if (record.phoneNumber.length() <= 0)
      return false;
    return true;
  }
}
