package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import elwyn.case_management.models.Subscription;
import elwyn.case_management.models.SubscriptionType;

public class SubscriptionController extends RecordController<Subscription> {
  CustomerController customerController;
  protected String tableName() { return "subscriptions"; }

  public SubscriptionController() {
    super();
    this.customerController = new CustomerController();
  }

  public List<Subscription> readRecords() {
    ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();
    try {
      String sql = "SELECT rowid, * from subscriptions"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
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
            
      while (rs.next()) {
        subscription = readResultSet(rs);
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return subscription;
  }

  public Subscription readResultSet(ResultSet rs) throws SQLException {
    Subscription record = new Subscription();
    long customerId = rs.getLong("customer");
    record.customer = customerController.readRecord(customerId);

    record.id = rs.getLong("rowid");
    record.subscriptionType = SubscriptionType.parseSelectedSubscriptionType(rs.getString("subscriptionType"));
    record.dateStarted = rs.getString("dateStarted");
    record.days = rs.getInt("days");
    return record;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Subscription record) throws SQLException {
    String sql="INSERT INTO subscriptions " + 
        "(customer, subscriptionType, dateStarted, days) " +
        "VALUES (?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Subscription record) throws SQLException {
    String sql="UPDATE subscriptions SET " +
        "customer=?, subscriptionType=?, dateStarted=?, days=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(5, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, Subscription record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setLong(1, record.customer.id);
    pStatement.setString(2, record.subscriptionType == null ? null : record.subscriptionType.toString());
    pStatement.setString(3, record.dateStarted);
    pStatement.setInt(4, record.days);
    return pStatement;
  }
    
  public boolean isRecordValid(Subscription record) {
    if (record.customer == null)
      return false;
    if (record.subscriptionType == null)
      return false;
    if (record.dateStarted.length() <= 0)
      return false;
    if (record.days == 0) // eTODO: how to know if days has been set
      return false;
    return true;
  }
}

