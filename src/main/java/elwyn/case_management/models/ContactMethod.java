package elwyn.case_management.models;

public enum ContactMethod {
  PHONE("Phone"),
  EMAIL("Email"),
  POST("Post"),
  OTHER("Other");

  private final String contactMethod;

  ContactMethod(String contactMethod) {
    this.contactMethod = contactMethod;
  }

  public static ContactMethod parseSelectedContactMethod(String contactMethod) {
    if (contactMethod.equals(PHONE.toString()))
      return ContactMethod.PHONE;
    if (contactMethod.equals(EMAIL.toString()))
      return ContactMethod.EMAIL;
    if (contactMethod.equals(POST.toString()))
      return ContactMethod.POST;
    if (contactMethod.equals(OTHER.toString()))
      return ContactMethod.OTHER;
    return null;
  }

  @Override
  public String toString() {
    return contactMethod;
  }
}
