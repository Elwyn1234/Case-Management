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
}
