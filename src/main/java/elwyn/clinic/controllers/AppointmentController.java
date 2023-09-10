package elwyn.clinic.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.swing.JComponent;

import elwyn.clinic.models.Appointment;
import elwyn.clinic.models.Log;
import elwyn.clinic.models.Role;
import elwyn.clinic.models.Severity;
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

  public List<Appointment> readRecordsReferredTo(long rowid) {
    ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    try {
      String sql = "SELECT rowid, * FROM appointments ORDER BY year DESC, month DESC, day DESC WHERE rowid=?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();

      while (rs.next()) {
        appointments.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
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

    record.id = rs.getLong("rowid");
    record.summary = rs.getString("summary");
    record.description = rs.getString("description");

    long customerId = rs.getLong("customer");
    record.customer = customerController.readRecord(customerId);

    long createdById = rs.getLong("createdBy");
    record.createdBy = userController.readRecord(createdById);

    long assignedToId = rs.getLong("assignedTo");
    if (!rs.wasNull()) {
      record.assignedTo = userController.readRecord(assignedToId);
    }

    long referredToId = rs.getLong("referredTo");
    if (!rs.wasNull()) {
      record.referredTo = userController.readRecord(referredToId);
    }

    int day = rs.getInt("day");
    int month = rs.getInt("month");
    int year = rs.getInt("year") - 1900;
    int second = rs.getInt("second");
    int minute = rs.getInt("minute");
    int hour = rs.getInt("hour");
    record.date = new Date(year, month, day, hour, minute, second);

    record.visitationStatus = VisitationStatus.parseSelectedStatus(rs.getString("visitationStatus"));

    record.closed = rs.getString("closed") == "true" ? true : false;

    record.prescribedHourlyFrequency = rs.getInt("prescribedHourlyFrequency");
    record.prescribedMgDose = rs.getInt("prescribedMgDose");
    record.prescribedMedication = rs.getString("prescribedMedication");

    if (loggedInUser.role == Role.CLERK && record.date.before(new Date())) {
      record.showDeleteButton = false;
      record.showUpdateButton = false;
    }
    return record;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Appointment record) throws SQLException {
  // public List<Contact> contacts
    String sql="INSERT INTO appointments " + 
        "(summary, " +
        "description, " +
        "customer, " +
        "assignedTo, " +
        "referredTo, " +
        "visitationStatus, " +
        "prescribedHourlyFrequency, " +
        "prescribedMgDose, " +
        "prescribedMedication, " +
        "closed, " +
        "createdBy, " +
        "day, " +
        "month, " +
        "year, " +
        "second, " +
        "minute, " +
        "hour) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    record.date = new Date();
    pStatement.setString(10, "false");
    pStatement.setLong(11, loggedInUser.id);
    pStatement.setInt(12, record.date.getDate());
    pStatement.setInt(13, record.date.getMonth());
    pStatement.setInt(14, record.date.getYear() + 1900);
    pStatement.setInt(15, record.date.getSeconds());
    pStatement.setInt(16, record.date.getMinutes());
    pStatement.setInt(17, record.date.getHours());

    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Appointment record) throws SQLException {
    String sql="UPDATE appointments SET " +
        "summary=?, " +
        "description=?, " +
        "customer=?, " +
        "assignedTo=? " +
        "referredTo=? " +
        "visitationStatus=? " +
        "prescribedHourlyFrequency=? " +
        "prescribedMgDose=? " +
        "prescribedMedication=? " +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(10, record.id);

    return pStatement;
  }

  private PreparedStatement PopulateCommonSqlParameters(String sql, Appointment record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.summary);
    pStatement.setString(2, record.description);
    pStatement.setLong(3, record.customer.id);
    pStatement.setLong(4, record.assignedTo.id);
    pStatement.setLong(5, record.referredTo.id);
    pStatement.setString(6, record.visitationStatus.toString());
    pStatement.setLong(7, record.prescribedHourlyFrequency);
    pStatement.setLong(8, record.prescribedMgDose);
    pStatement.setString(9, record.prescribedMedication);
    return pStatement;
  }

  public JComponent closeRecord(Long rowid) {
    try {
      String sql="UPDATE appointments SET " +
          "closed=? " +
          "WHERE rowid=?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setString(1, "true");
      pStatement.setLong(2, rowid);
      pStatement.executeUpdate();

      Log log = new Log();
      log.severity = Severity.LOG;
      log.user = loggedInUser.id;
      log.log = "Appointment Closed: " + rowid;
      logController.createRecord(log);
    }
    catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }

    return null;
  }

  public Boolean shouldShowButton(Appointment record) {
    if (record.closed)
      return false;
    if (loggedInUser.role == Role.SP && record.referredTo != null && record.referredTo.id == loggedInUser.id)
      return true;
    if (loggedInUser.role == Role.CLERK && record.visitationStatus == VisitationStatus.REQUIRES_MEDICATION)
      return true;
    if (loggedInUser.role == Role.ADMIN)
      return true;
    return false;
  }
}
