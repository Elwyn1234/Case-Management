package elwyn.clinic.models;

public enum Role {
  ADMIN("Admin"),
  CLERK("Clerk"),
  GP("GP"),
  SP("SP");

  private final String role;

  private Role(String role) {
    this.role = role;
  }

  public static Role parseSelectedRole(String role) {
    if (role == null)
      return null;
    if (role.equals(ADMIN.toString()))
      return ADMIN;
    if (role.equals(CLERK.toString()))
      return CLERK;
    if (role.equals(GP.toString()))
      return GP;
    if (role.equals(SP.toString()))
      return SP;
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
