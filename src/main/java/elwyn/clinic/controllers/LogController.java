package elwyn.clinic.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import elwyn.clinic.models.Log;
import elwyn.clinic.models.Severity;
import elwyn.clinic.models.User;

public class LogController extends RecordController<Log> {
  protected String tableName() { return "logs"; }
  protected String recordName() { return "Log"; }
  protected Boolean logMe() { return false; }

  public LogController(User loggedInUser) {
    super(loggedInUser, null);
  }

  public List<Log> readRecords(int page) {
    ArrayList<Log> logs = new ArrayList<Log>();
    try {
      String sql = "SELECT rowid, * FROM logs ORDER BY year DESC, month DESC, day DESC, hour DESC, minute DESC, second DESC LIMIT ?,?"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setInt(1, page * 1000);
      pStatement.setInt(2, 1000);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        logs.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return logs;
  }

  public Log readRecord(long rowid) {
    Log log = new Log();
    try {
      String sql = "SELECT rowid, * FROM logs WHERE rowid=?"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();

      int count = 0;
      while (rs.next()) {
        count++;
        log = readResultSet(rs);
      }
      if (count == 0)
        log = null; // No record found
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return log;
  }

  public Log readResultSet(ResultSet rs) throws SQLException {
    Log record = new Log();

    record.id = rs.getLong("rowid");
    record.user = rs.getLong("user");
    record.severity = Severity.parseSelectedSeverity(rs.getString("severity"));
    record.log = rs.getString("log");

    int day = rs.getInt("day");
    int month = rs.getInt("month");
    int year = rs.getInt("year") - 1900; // dont ask
    int second = rs.getInt("second");
    int minute = rs.getInt("minute");
    int hour = rs.getInt("hour");
    record.date = new Date(year, month, day, hour, minute, second);

    return record;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Log record) throws SQLException {
  // public List<Contact> contacts
    String sql="INSERT INTO logs " + 
        "(user, " +
        "severity, " +
        "log, " +
        "day, " +
        "month, " +
        "year, " +
        "second, " +
        "minute, " +
        "hour) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Log record) throws SQLException {
    throw new SQLException("Abstract method not implemented");
  }

  private PreparedStatement PopulateCommonSqlParameters(String sql, Log record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    if (loggedInUser == null) {
      pStatement.setNull(1, Types.INTEGER);
    } else {
      pStatement.setLong(1, loggedInUser.id);
    }
    pStatement.setString(2, record.severity.toString());
    pStatement.setString(3, record.log);

    record.date = new Date();
    pStatement.setInt(4, record.date.getDate());
    pStatement.setInt(5, record.date.getMonth());
    pStatement.setInt(6, record.date.getYear() + 1900);
    pStatement.setInt(7, record.date.getSeconds());
    pStatement.setInt(8, record.date.getMinutes());
    pStatement.setInt(9, record.date.getHours());
    return pStatement;
  }
}
