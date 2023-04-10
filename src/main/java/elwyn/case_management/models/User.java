package elwyn.case_management.models;

public class User extends Record {
  public String name;
  public String username;
  public String password; // eTODO: hashed?
  public Role role;
}
