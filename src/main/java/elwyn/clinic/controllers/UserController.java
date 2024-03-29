package elwyn.clinic.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import elwyn.clinic.models.Role;
import elwyn.clinic.models.User;

public class UserController extends RecordController<User> {
  protected String tableName() { return "users"; }
  protected String recordName() { return "User"; }
  protected Boolean logMe() { return true; }
    
  public UserController(User loggedInUser) {
    super(loggedInUser, new LogController(loggedInUser));
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
    if (user == null)
      return false;
    if (user.username == null || !user.username.equals(username))
      return false;
    if (password.equals(user.password)) // eTODO
      return true;
    if (BCrypt.checkpw(password, user.password))
      return true;
    return false;
  }

  protected PreparedStatement buildInsertPreparedStatement(User record) throws SQLException {
    String sql = "INSERT INTO users (name, username, role, password) VALUES (?, ?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    String hashed = BCrypt.hashpw(record.password, BCrypt.gensalt());
    pStatement.setString(4, hashed);
    return pStatement;
  }
  protected PreparedStatement buildUpdatePreparedStatement(User record) throws SQLException {
    String sql = "UPDATE users SET name=?, username=?, role=?, password=IIF(?='', password, ?) where rowid=?;";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    String hashed = BCrypt.hashpw(record.password, BCrypt.gensalt());
    pStatement.setString(4, record.password);
    pStatement.setString(5, hashed);
    pStatement.setLong(6, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, User record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.name);
    pStatement.setString(2, record.username);
    pStatement.setString(3, record.role.toString());
    return pStatement;
  }

  protected void recursiveDelete(long rowid) {}
}

