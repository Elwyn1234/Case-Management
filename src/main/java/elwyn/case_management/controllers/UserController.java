package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import elwyn.case_management.models.Role;
import elwyn.case_management.models.User;

public class UserController extends RecordController<User> {
  protected String tableName() { return "users"; }
    
  public List<User> readRecords() {
    List<User> users = new ArrayList<User>();
    try {
      String sql = "SELECT rowid, * from users";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        users.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return users;
  }

  public User readRecord(long rowid) {
    User user = new User();
    try {
      String sql = "SELECT rowid, * from users WHERE rowid=?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        user = readResultSet(rs);
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return user;
  }

  private User readResultSet(ResultSet rs) throws SQLException {
    User user = new User();
    user.id = rs.getLong("rowid");
    user.name = rs.getString("name");
    user.username = rs.getString("username");
    user.password = rs.getString("password");
    user.role = Role.parseSelectedRole(rs.getString("role"));
    return user;
  }
    
  public boolean isRecordValid(User user) {
    if (user.name.length() <= 0) {
      return false;
    }
    if (user.username.length() <= 0) { // eTODO: Check username is unique
      return false;
    }
    if (user.password.length() <= 0) {
      return false; // eTODO: password validation
    }
    if (user.role == null) {
      return false;
    }
    return true;
  }

  protected PreparedStatement buildInsertPreparedStatement(User record) throws SQLException {
    String sql = "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }
  protected PreparedStatement buildUpdatePreparedStatement(User record) throws SQLException {
    String sql = "UPDATE users SET name=?, username=?, password=?, role=? where rowid=?;";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(5, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, User record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.name);
    pStatement.setString(2, record.username);
    pStatement.setString(3, record.password); // eTODO: hashing
    pStatement.setString(4, record.role.toString());
    return pStatement;
  }

  protected void recursiveDelete(long rowid) {}
}

