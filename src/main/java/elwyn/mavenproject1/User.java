package elwyn.mavenproject1;

public class User {
  long id;
  String name;
  String username;
  String password; // eTODO: hashed?
  Role role; // eTODO: enum

  enum Role {
    AGENT,
    LEADER,
    ADMIN
  }
}
