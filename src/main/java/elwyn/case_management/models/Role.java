package elwyn.case_management.models;

public enum Role {
  AGENT("Agent"),
  LEADER("Leader"),
  ADMIN("Admin");

  private final String role;

  private Role(String role) {
    this.role = role;
  }

  public static Role parseSelectedRole(String role) {
    if (role == null)
      return null;
    if (role.equals(AGENT.toString()))
      return AGENT;
    if (role.equals(LEADER.toString()))
      return LEADER;
    if (role.equals(ADMIN.toString()))
      return ADMIN;
    return null;
  }

  public static String[] stringValues() {
    String[] array = new String[values().length];
    for (int i = 0; i < values().length; i++) {
      array[i] = values()[i].toString();
    }
    return array;
  }

  @Override
  public String toString() {
    return role;
  }
}
