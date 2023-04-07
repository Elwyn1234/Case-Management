package elwyn.case_management.models;

public class User {
  public long id;
  public String name;
  public String username;
  public String password; // eTODO: hashed?
  public Role role; // eTODO: enum

  public enum Role {
    AGENT,
    LEADER,
    ADMIN
  }
}
