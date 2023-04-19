package elwyn.case_management.models;

import java.util.Date;

public class Customer extends Record {
  public String firstName;
  public String secondName;
  public String sirname;
  public Gender gender;
  public Date dateOfBirth;
  public String otherNotes;
  public String email;
  public String phoneNumber;
  public String address;
  public String city;
  public String postcode;
  public String country;

  public String fullNameAndId() {
    boolean hasSecondName = (secondName != "" | secondName != null);
    return firstName + " " + (hasSecondName ? secondName + " " : "") + sirname + " (" + id + ")";
  }
}
