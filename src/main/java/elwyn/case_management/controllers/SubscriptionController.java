package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import elwyn.case_management.models.Frequency;
import elwyn.case_management.models.Subscription;
import elwyn.case_management.models.User;

public class SubscriptionController extends RecordController<Subscription> {
  CustomerController customerController;
  protected String tableName() { return "subscriptions"; }

  public SubscriptionController(User loggedInUser) {
    super(loggedInUser);
    this.customerController = new CustomerController(loggedInUser);
  }

  public List<Subscription> readRecords(int page) {
    ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();
    try {
      String sql = "SELECT rowid, * FROM subscriptions LIMIT ?,?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setInt(1, page * PAGE_SIZE);
      pStatement.setInt(2, PAGE_SIZE);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        subscriptions.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return subscriptions;
  }

  public Subscription readRecord(long rowid) {
    Subscription subscription = new Subscription();
    try {
      String sql = "SELECT rowid, * FROM subscriptions WHERE rowid=?"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();
            
      int count = 0;
      while (rs.next()) {
        count++;
        subscription = readResultSet(rs);
      }
      if (count == 0)
        subscription = null; // No record found
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return subscription;
  }

  public Subscription readResultSet(ResultSet rs) throws SQLException {
    Subscription record = new Subscription();
    record.id = rs.getLong("rowid");
    record.name = rs.getString("name");
    record.description = rs.getString("description");
    record.frequency = Frequency.parseSelectedFrequency(rs.getString("frequency"));
    record.price = rs.getInt("price");
    return record;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Subscription record) throws SQLException {
    String sql="INSERT INTO subscriptions " + 
        "(name, description, frequency, price) " +
        "VALUES (?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Subscription record) throws SQLException {
    String sql="UPDATE subscriptions SET " +
        "name=?, description=?, frequency=?, price=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(5, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, Subscription record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.name);
    pStatement.setString(2, record.description);
    pStatement.setString(3, record.frequency.toString());
    pStatement.setInt(4, record.price);
    return pStatement;
  }
}

