package elwyn.clinic.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import elwyn.clinic.models.Medicine;
import elwyn.clinic.models.User;

public class MedicineController extends RecordController<Medicine> {
  protected String tableName() { return "medicines"; }
  protected String recordName() { return "Medicine"; }
  protected Boolean logMe() { return true; }

  public MedicineController(User loggedInUser) {
    super(loggedInUser, new LogController(loggedInUser));
  }

  public List<Medicine> readRecords(int page) {
    ArrayList<Medicine> medicines = new ArrayList<Medicine>();
    try {
      String sql = "SELECT rowid, * FROM medicines ORDER BY name ASC LIMIT ?,?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setInt(1, page * PAGE_SIZE);
      pStatement.setInt(2, PAGE_SIZE);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        medicines.add(readResultSet(rs));
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return medicines;
  }

  public Medicine readRecord(long rowid) {
    Medicine medicine = new Medicine();
    try {
      String sql = "SELECT rowid, * FROM medicines WHERE rowid=?";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      pStatement.setLong(1, rowid);
      ResultSet rs = pStatement.executeQuery();
            
      int count = 0;
      while (rs.next()) {
        count++;
        medicine = readResultSet(rs);
      }
      if (count == 0)
        medicine = null; // No record found
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return medicine;
  }

  public Medicine readResultSet(ResultSet rs) throws SQLException {
    Medicine medicine = new Medicine();
    medicine.id = rs.getLong("rowid");
    medicine.name = rs.getString("name");
    medicine.description = rs.getString("description");
    return medicine;
  }

  protected void recursiveDelete(long rowid) {}

  protected PreparedStatement buildInsertPreparedStatement(Medicine record) throws SQLException {
    String sql="INSERT INTO medicines " + 
        "(name, description) " +
        "VALUES (?, ?);";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    return pStatement;
  }

  protected PreparedStatement buildUpdatePreparedStatement(Medicine record) throws SQLException {
    String sql="UPDATE medicines SET " +
        "name=?, description=?" +
        "WHERE rowid=?";
    PreparedStatement pStatement = PopulateCommonSqlParameters(sql, record);
    pStatement.setLong(3, record.id);
    return pStatement;
  }
  private PreparedStatement PopulateCommonSqlParameters(String sql, Medicine record) throws SQLException {
    PreparedStatement pStatement = conn.prepareStatement(sql);
    pStatement.setString(1, record.name);
    pStatement.setString(2, record.description);
    return pStatement;
  }
}
