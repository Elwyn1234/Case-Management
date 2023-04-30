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
    String indent = "    ";
    for (int i = 0; i < depth; i++) {
      indent += indent;
    }
    String val = "{\n";
    val += indent + "id: " + id + "\n";
    val += indent + "firstName: " + firstName + "\n";
    val += indent + "secondName: " + secondName + "\n";
    val += indent + "sirname: " + sirname + "\n";
    val += indent + "gender: " + gender + "\n";
    val += indent + "dateOfBirth: " + dateOfBirth + "\n";
    val += indent + "otherNotes: " + otherNotes + "\n";
    val += indent + "email: " + email + "\n";
    val += indent + "phoneNumber: " + phoneNumber + "\n";
    val += indent + "address: " + address + "\n";
    val += indent + "city: " + city + "\n";
    val += indent + "postcode: " + postcode + "\n";
    val += indent + "country: " + country + "\n";
    val += "}";
    return val;
  }
}
