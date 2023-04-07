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

  public static Gender parseSelectedGender(String rawGender) {
    if (rawGender == null)
      return null;
    if (rawGender == "Non-Binary")
      return Gender.NON_BINARY;
    if (rawGender == "Male")
      return Gender.MALE;
    if (rawGender == "Female")
      return Gender.FEMALE;
    if (rawGender == "Other")
      return Gender.OTHER;
    return null;
  }
}
