package elwyn.case_management.models;

public enum Gender {
  NON_BINARY("Non-Binary"),
  MALE("Male"),
  FEMALE("Female"),
  OTHER("Other");

  private final String gender;

  Gender(String gender) {
    this.gender = gender;
  }

  public static Gender parseSelectedGender(String gender) {
    if (gender == null)
      return null;
    if (gender.equals(NON_BINARY.toString()))
      return NON_BINARY;
    if (gender.equals(MALE.toString()))
      return MALE;
    if (gender.equals(FEMALE.toString()))
      return FEMALE;
    if (gender.equals(OTHER.toString()))
      return OTHER;
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
    return gender;
  }
}
