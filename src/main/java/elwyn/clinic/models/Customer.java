package elwyn.clinic.models;

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

  public VisitationStatus visitationStatus;
  public User referredTo;
  public String healthConditions;
  public String currentPrescriptions;
  public String allergies;

  public String fullNameAndId() {
    boolean hasSecondName = (secondName != null && !secondName.equals(""));
    return firstName + " " + (hasSecondName ? secondName + " " : "") + sirname + " (" + id + ")";
  }

  public String fullAddress() {
    boolean hasAddress = (address != null && !address.equals(""));
    boolean hasCity = (city != null && !city.equals(""));
    boolean hasPostcode = (postcode != null && !postcode.equals(""));
    boolean hasCountry = (country != null && !country.equals(""));
    String fullAddress = "";
    if (hasAddress) fullAddress += address + " ";
    if (hasCity) fullAddress += city + " ";
    if (hasPostcode) fullAddress += postcode + " ";
    if (hasCountry) fullAddress += country + " ";
    return fullAddress;
  }

  @Override
  public String toString(int depth) {
    String oneIndent = "        ";
    String indent1 = "";
    String indent2 = oneIndent;
    for (int i = 0; i < depth; i++) {
      indent1 += oneIndent;
      indent2 += oneIndent;
    }
    String val = "{\n";
    if (id != 0)
      val += indent2 + "id: " + id + "\n";
    val += indent2 + "firstName: " + firstName + "\n";
    val += indent2 + "secondName: " + secondName + "\n";
    val += indent2 + "sirname: " + sirname + "\n";
    val += indent2 + "gender: " + gender + "\n";
    val += indent2 + "dateOfBirth: " + dateOfBirth + "\n";
    val += indent2 + "otherNotes: " + otherNotes + "\n";
    val += indent2 + "email: " + email + "\n";
    val += indent2 + "phoneNumber: " + phoneNumber + "\n";
    val += indent2 + "address: " + address + "\n";
    val += indent2 + "city: " + city + "\n";
    val += indent2 + "postcode: " + postcode + "\n";
    val += indent2 + "country: " + country + "\n";
    val += indent1 + "}";
    return val;
  }
}
