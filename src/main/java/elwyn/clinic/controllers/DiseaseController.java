package elwyn.clinic.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import elwyn.clinic.models.Disease;
import elwyn.clinic.models.Category;
import elwyn.clinic.models.User;

public class DiseaseController extends RecordController<Disease> {
  Function<List<Disease>, List<Disease>> filter;
  protected String tableName() { return "diseases"; }
  protected String recordName() { return "Disease"; }
  protected Boolean logMe() { return true; }

  public DiseaseController(User loggedInUser, Function<List<Disease>, List<Disease>> filter) {
    super(loggedInUser, new LogController(loggedInUser));
    this.filter = filter;
  }

  public List<Disease> readRecords(int page) {
    ArrayList<Disease> diseases = new ArrayList<Disease>();
    try {
      String sql = "SELECT rowid, * FROM diseases ORDER BY name LIMIT ?,?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setInt(1, page * PAGE_SIZE);
      pStatement.setInt(2, PAGE_SIZE);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        diseases.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    if (filter != null)
      return filter.apply(diseases);
    else
      return diseases;
  }

  public List<Disease> readRecords(long userId) {
    ArrayList<Disease> diseases = new ArrayList<Disease>();
    try {
      String sql = "SELECT category FROM diseases";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, userId);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        Disease record = new Disease();
        record.category = Category.parseSelectedCategory(rs.getString("category"));
        diseases.add(record); }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    if (filter != null)
      return filter.apply(diseases);
    else
      return diseases;
  }

  public Disease readRecord(long rowid) {
    Disease diseaseRecord = new Disease();
    try {
      String sql = "SELECT rowid, * FROM diseases WHERE rowid=?"; // eTODO: Use tableName function
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();
            
      int count = 0;
      while (rs.next()) {
        count++;
        diseaseRecord = readResultSet(rs);
      }
      if (count == 0)
        diseaseRecord = null; // No record found
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return diseaseRecord;
  }

  public Disease readResultSet(ResultSet rs) throws SQLException {
    Disease record = new Disease();

    record.id = rs.getLong("rowid");
    record.name = rs.getString("name");
    record.description = rs.getString("description");
    record.category = Category.parseSelectedCategory(rs.getString("category"));
    return record;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Disease record) throws SQLException {
  // public List<Contact> contacts
    String sql="INSERT INTO diseases " + 
        "(name, " +
        "description, " +
        "category) " +
        "VALUES (?, ?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Disease record) throws SQLException {
    String sql="UPDATE diseases SET " +
        "name=?, " +
        "description=?, " +
        "category=? " +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(4, record.id);

    return pStatement;
  }

  private PreparedStatement PopulateCommonSqlParameters(String sql, Disease record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.name);
    pStatement.setString(2, record.description);
    pStatement.setString(3, record.category.toString());
    return pStatement;
  }
}
