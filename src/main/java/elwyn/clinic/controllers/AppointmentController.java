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
      String sql = "SELECT rowid, * FROM appointments WHERE rowid=? ORDER BY year DESC, month DESC, day DESC";
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

  public List<String> readPharmacies() {
    ArrayList<String> appointments = new ArrayList<String>();
    try {
      String sql = "SELECT DISTINCT pharmacy FROM appointments";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      ResultSet rs = pStatement.executeQuery();

      while (rs.next()) {
        appointments.add(rs.getString("pharmacy"));
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
    record.pharmacy = rs.getString("pharmacy");

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

    int dayCreated = rs.getInt("day");
    int monthCreated = rs.getInt("month");
    int yearCreated = rs.getInt("year") - 1900;
    int secondCreated = rs.getInt("second");
    int minuteCreated = rs.getInt("minute");
    int hourCreated = rs.getInt("hour");
    record.dateCreated = new Date(yearCreated, monthCreated, dayCreated, hourCreated, minuteCreated, secondCreated);

    record.visitationStatus = VisitationStatus.parseSelectedStatus(rs.getString("visitationStatus"));

    record.closed = rs.getString("closed");

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
        "pharmacy, " +
        "customer, " +
        "assignedTo, " +
        "referredTo, " +
        "visitationStatus, " +
        "prescribedHourlyFrequency, " +
        "prescribedMgDose, " +
        "prescribedMedication, " +
        "day, " +
        "month, " +
        "year, " +
        "second, " +
        "minute, " +
        "hour, " +
        "closed, " +
        "createdBy, " +
        "dayCreated, " +
        "monthCreated, " +
        "yearCreated, " +
        "secondCreated, " +
        "minuteCreated, " +
        "hourCreated) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    record.dateCreated = new Date();
    pStatement.setString(17, "false");
    pStatement.setLong(18, loggedInUser.id);
    pStatement.setInt(19, record.dateCreated.getDate());
    pStatement.setInt(20, record.dateCreated.getMonth());
    pStatement.setInt(21, record.dateCreated.getYear() + 1900);
    pStatement.setInt(22, record.dateCreated.getSeconds());
    pStatement.setInt(23, record.dateCreated.getMinutes());
    pStatement.setInt(24, record.dateCreated.getHours());

    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Appointment record) throws SQLException {
    String sql="UPDATE appointments SET " +
        "summary=?, " +
        "description=?, " +
        "pharmacy=?, " +
        "customer=?, " +
        "assignedTo=?, " +
        "referredTo=?, " +
        "visitationStatus=?, " +
        "prescribedHourlyFrequency=?, " +
        "prescribedMgDose=?, " +
        "prescribedMedication=?, " +
        "dayCreated=?, " +
        "monthCreated=?, " +
        "yearCreated=?, " +
        "secondCreated=?, " +
        "minuteCreated=?, " +
        "hourCreated=?, " +
        "closed=? " +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setString(17, record.closed);
    pStatement.setLong(18, record.id);

    return pStatement;
  }

  private PreparedStatement PopulateCommonSqlParameters(String sql, Appointment record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.summary);
    pStatement.setString(2, record.description);
    pStatement.setString(3, record.pharmacy);
    pStatement.setLong(4, record.customer.id);
    pStatement.setLong(5, record.assignedTo.id);
    pStatement.setLong(6, record.referredTo.id);
    pStatement.setString(7, record.visitationStatus == null ? null : record.visitationStatus.toString());
    pStatement.setLong(8, record.prescribedHourlyFrequency);
    pStatement.setLong(9, record.prescribedMgDose);
    pStatement.setString(10, record.prescribedMedication);
    pStatement.setInt(11, record.date.getDate());
    pStatement.setInt(12, record.date.getMonth());
    pStatement.setInt(13, record.date.getYear() + 1900);
    pStatement.setInt(14, record.date.getSeconds());
    pStatement.setInt(15, record.date.getMinutes());
    pStatement.setInt(16, record.date.getHours());
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
    if (record.closed == "Complete")
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
