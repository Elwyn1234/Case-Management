package elwyn.clinic.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import elwyn.clinic.models.Appointment;
import elwyn.clinic.models.User;
import elwyn.clinic.models.VisitationStatus;

public class AppointmentController extends RecordController<Appointment> {
  public CustomerController customerController;
  public UserController userController;
  Function<List<Appointment>, List<Appointment>> filter;
  protected String tableName() { return "appointments"; }
  protected String recordName() { return "Appointment"; }
  protected Boolean logMe() { return true; }

  public AppointmentController(User loggedInUser, Function<List<Appointment>, List<Appointment>> filter) {
    super(loggedInUser, new LogController(loggedInUser));
    this.customerController = new CustomerController(loggedInUser);
    this.userController = new UserController(loggedInUser);
    this.filter = filter;
  }

  public List<Appointment> readRecords(int page) {
    ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    try {
      String sql = "SELECT rowid, * FROM appointments ORDER BY year DESC, month DESC, day DESC LIMIT ?,?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setInt(1, page * PAGE_SIZE);
      pStatement.setInt(2, PAGE_SIZE);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        appointments.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    if (filter != null)
      return filter.apply(appointments);
    else
      return appointments;
  }

  public Appointment readRecord(long rowid) {
    Appointment appointmentRecord = new Appointment();
    try {
      String sql = "SELECT rowid, * FROM appointments WHERE rowid=?"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();
            
      int count = 0;
      while (rs.next()) {
        count++;
        appointmentRecord = readResultSet(rs);
      }
      if (count == 0)
        appointmentRecord = null; // No record found
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return appointmentRecord;
  }

  public Appointment readResultSet(ResultSet rs) throws SQLException {
    Appointment record = new Appointment();
    long customerId = rs.getLong("customer");
    record.customer = customerController.readRecord(customerId);
    long createdById = rs.getLong("createdBy");
    record.createdBy = userController.readRecord(createdById);
    long referredToId = rs.getLong("referredTo");
    if (!rs.wasNull()) {
      record.referredTo = userController.readRecord(referredToId);
    }

    record.id = rs.getLong("rowid");
    record.summary = rs.getString("summary");
    record.description = rs.getString("description");
    int day = rs.getInt("day");
    int month = rs.getInt("month");
    int year = rs.getInt("year") - 1900;
    int second = rs.getInt("second");
    int minute = rs.getInt("minute");
    int hour = rs.getInt("hour");
    record.visitationStatus = VisitationStatus.parseSelectedStatus(rs.getString("visitationStatus"));
    record.date = new Date(year, month, day, hour, minute, second);
    return record;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Appointment record) throws SQLException {
  // public List<Contact> contacts
    String sql="INSERT INTO appointments " + 
        "(summary, " +
        "description, " +
        "customer, " +
        "referredTo, " +
        "createdBy, " +
        "day, " +
        "month, " +
        "year, " +
        "second, " +
        "minute, " +
        "hour, " +
        "visitationStatus) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    record.date = new Date();
    pStatement.setLong(5, loggedInUser.id);
    pStatement.setInt(6, record.date.getDate());
    pStatement.setInt(7, record.date.getMonth());
    pStatement.setInt(8, record.date.getYear() + 1900);
    pStatement.setInt(9, record.date.getSeconds());
    pStatement.setInt(10, record.date.getMinutes());
    pStatement.setInt(11, record.date.getHours());

    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Appointment record) throws SQLException {
    String sql="UPDATE appointments SET " +
        "summary=?, " +
        "description=?, " +
        "customer=?, " +
        "referredTo=? " +
        "visitationStatus=? " +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(6, record.id);

    return pStatement;
  }

  private PreparedStatement PopulateCommonSqlParameters(String sql, Appointment record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.summary);
    pStatement.setString(2, record.description);
    pStatement.setLong(3, record.customer.id);
    pStatement.setLong(4, record.referredTo.id);
    pStatement.setString(5, record.visitationStatus.toString());
    return pStatement;
  }
}
