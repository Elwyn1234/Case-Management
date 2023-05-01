package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import elwyn.case_management.models.SubscriptionToCustomer;
import elwyn.case_management.models.User;

public class SubscriptionToCustomerController extends RecordController<SubscriptionToCustomer> {
  long customer;

  protected String tableName() { return "subscriptionToCustomer"; }
  protected String recordName() { return "Subscription to Customer link"; }
  protected Boolean logMe() { return true; }

  public SubscriptionToCustomerController(User loggedInUser, long customer) {
    super(loggedInUser, new LogController(loggedInUser));
    this.customer = customer;
  }

  public List<SubscriptionToCustomer> readRecords(int page) {
    ArrayList<SubscriptionToCustomer> records = new ArrayList<SubscriptionToCustomer>();
    try {
      String sql = "SELECT rowid, * FROM subscriptionToCustomer WHERE customer=? AND yearEnded IS NULL LIMIT ?,?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, customer);
      pStatement.setInt(2, page * PAGE_SIZE);
      pStatement.setInt(3, PAGE_SIZE);
      ResultSet rs = pStatement.executeQuery();

      while (rs.next()) {
        records.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return records;
  }

  public SubscriptionToCustomer readRecord(long rowid) {
    SubscriptionToCustomer subscription = new SubscriptionToCustomer();
    try {
      String sql = "SELECT rowid, * FROM subscriptionToCustomer WHERE rowid=?"; // eTODO: Use tableName function
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

  public SubscriptionToCustomer readResultSet(ResultSet rs) throws SQLException {
    SubscriptionToCustomer record = new SubscriptionToCustomer();
    record.id = rs.getLong("rowid");
    record.subscription = rs.getLong("subscription");
    record.customer = rs.getLong("customer");

    int dayStarted = rs.getInt("dayStarted");
    int monthStarted = rs.getInt("monthStarted");
    int yearStarted = rs.getInt("yearStarted") - 1900;
    int hourStarted = rs.getInt("hourStarted");
    int minuteStarted = rs.getInt("minuteStarted");
    int secondStarted = rs.getInt("secondStarted");
    record.dateStarted = new Date(yearStarted, monthStarted, dayStarted, hourStarted, minuteStarted, secondStarted);

    int dayEnded = rs.getInt("dayEnded");
    int monthEnded = rs.getInt("monthEnded");
    int yearEnded = rs.getInt("yearEnded") - 1900;
    int hourEnded = rs.getInt("hourEnded");
    int minuteEnded = rs.getInt("minuteEnded");
    int secondEnded = rs.getInt("secondEnded");
    if (!rs.wasNull()) {
      record.dateEnded = new Date(yearEnded, monthEnded, dayEnded, hourEnded, minuteEnded, secondEnded);
    }

    return record;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(SubscriptionToCustomer record) throws SQLException {
    String sql="INSERT INTO subscriptionToCustomer (" + 
        "subscription, " +
        "customer, " +
        "dayStarted, " +
        "monthStarted, " +
        "yearStarted, " +
        "hourStarted, " +
        "minuteStarted, " +
        "secondStarted) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setLong(1, record.subscription);
    pStatement.setLong(2, record.customer);
    pStatement.setInt(3, record.dateStarted.getDate());
    pStatement.setInt(4, record.dateStarted.getMonth());
    pStatement.setInt(5, record.dateStarted.getYear() + 1900);
    pStatement.setInt(6, record.dateStarted.getSeconds());
    pStatement.setInt(7, record.dateStarted.getMinutes());
    pStatement.setInt(8, record.dateStarted.getHours());
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(SubscriptionToCustomer record) throws SQLException {
    String sql="UPDATE subscriptionToCustomer SET " +
        "dayEnded=?, " +
        "monthEnded=?, " +
        "yearEnded=?, " +
        "hourEnded=?, " +
        "minuteEnded=?, " +
        "secondEnded=? " +
        "WHERE rowid=?";
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setInt(1, record.dateEnded.getDate());
    pStatement.setInt(2, record.dateEnded.getMonth());
    pStatement.setInt(3, record.dateEnded.getYear() + 1900);
    pStatement.setInt(4, record.dateEnded.getSeconds());
    pStatement.setInt(5, record.dateEnded.getMinutes());
    pStatement.setInt(6, record.dateEnded.getHours());
    pStatement.setLong(7, record.id);
    return pStatement;
  }
}

