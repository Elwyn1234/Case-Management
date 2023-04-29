package elwyn.case_management.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import elwyn.case_management.models.Role;
import elwyn.case_management.models.User;

public class UserController extends RecordController<User> {
  protected String tableName() { return "users"; }
    
  public UserController(User loggedInUser) {
    super(loggedInUser);
  }

  public List<User> readRecords(int page) {
    List<User> users = new ArrayList<User>();
    try {
      String sql = "SELECT rowid, * FROM users ORDER BY name ASC LIMIT ?,?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setInt(1, PAGE_SIZE * page);
      pStatement.setInt(2, PAGE_SIZE);
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

  public User readRecord(String username) { // eTODO: can we abstract this
    User user = new User();
    try {
      String sql = "SELECT rowid, * from users WHERE username=?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setString(1, username);
      ResultSet rs = pStatement.executeQuery();
            
      int count = 0;
      while (rs.next()) {
        count++;
        user = readResultSet(rs);
      }
      if (count == 0)
        user = null; // No record found
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return user;
  }

  public User readRecord(long rowid) {
    User user = new User();
    try {
      String sql = "SELECT rowid, * from users WHERE rowid=?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();
            
      int count = 0;
      while (rs.next()) {
        count++;
        user = readResultSet(rs);
      }
      if (count == 0)
        user = null; // No record found
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

  public boolean areCredentialsValid(String username, String password) {
    User user = readRecord(username);
    if (user.username == null)
      return false;
    if (BCrypt.checkpw(password, user.password))
      return true;
    return false;
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
    String hashed = BCrypt.hashpw(record.password, BCrypt.gensalt());
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.name);
    pStatement.setString(2, record.username);
    pStatement.setString(3, hashed); // eTODO: hashing
    pStatement.setString(4, record.role.toString());
    return pStatement;
  }

  protected void recursiveDelete(long rowid) {}
}

