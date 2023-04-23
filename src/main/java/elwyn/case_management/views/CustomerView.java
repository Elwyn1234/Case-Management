package elwyn.case_management.views;

import java.util.Date;

import java.awt.Color;

import javax.swing.JList;
import javax.swing.text.JTextComponent;
import javax.swing.JComponent;
import javax.swing.JLabel;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.Gender;

public class CustomerView extends RecordView<Customer> {
  JTextComponent firstName;
  JTextComponent secondName;
  JTextComponent sirname;
  JList<String> genderList;
  JTextComponent dayOfBirth;
  JTextComponent monthOfBirth;
  JTextComponent yearOfBirth;
  JTextComponent otherNotes;
  JTextComponent email;
  JTextComponent phoneNumber;
  JTextComponent address;
  JTextComponent city;
  JTextComponent postcode;
  JTextComponent country;

  JLabel firstNameValidityMessage = new JLabel();
  JLabel secondNameValidityMessage = new JLabel();
  JLabel sirnameValidityMessage = new JLabel();
  JLabel dayOfBirthValidityMessage = new JLabel();
  JLabel monthOfBirthValidityMessage = new JLabel();
  JLabel yearOfBirthValidityMessage = new JLabel();
  JLabel dateOfBirthValidityMessage = new JLabel();
  JLabel otherNotesValidityMessage = new JLabel();
  JLabel emailValidityMessage = new JLabel();
  JLabel phoneNumberValidityMessage = new JLabel();
  JLabel addressValidityMessage = new JLabel();
  JLabel cityValidityMessage = new JLabel();
  JLabel postcodeValidityMessage = new JLabel();
  JLabel countryValidityMessage = new JLabel();

  protected String pageTitle() { return "Customers"; }
  protected String tabNameOfViewRecords() { return "View Customers"; }
  protected String tabNameOfCreateRecord() { return "Create Customer"; }
  protected String tabNameOfEditRecord() { return "Edit Customer"; }

  public CustomerView(RecordController<Customer> controller) {
    super(controller, null);

    firstNameValidityMessage.setForeground(Color.RED);
    secondNameValidityMessage.setForeground(Color.RED);
    sirnameValidityMessage.setForeground(Color.RED);
    dayOfBirthValidityMessage.setForeground(Color.RED);
    monthOfBirthValidityMessage.setForeground(Color.RED);
    yearOfBirthValidityMessage.setForeground(Color.RED);
    dateOfBirthValidityMessage.setForeground(Color.RED);
    otherNotesValidityMessage.setForeground(Color.RED);
    emailValidityMessage.setForeground(Color.RED);
    phoneNumberValidityMessage.setForeground(Color.RED);
    addressValidityMessage.setForeground(Color.RED);
    cityValidityMessage.setForeground(Color.RED);
    postcodeValidityMessage.setForeground(Color.RED);
    countryValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Customer record) {
    firstNameValidityMessage.setVisible(false);
    secondNameValidityMessage.setVisible(false);
    sirnameValidityMessage.setVisible(false);
    dayOfBirthValidityMessage.setVisible(false);
    monthOfBirthValidityMessage.setVisible(false);
    yearOfBirthValidityMessage.setVisible(false);
    dateOfBirthValidityMessage.setVisible(false);
    otherNotesValidityMessage.setVisible(false);
    emailValidityMessage.setVisible(false);
    phoneNumberValidityMessage.setVisible(false);
    addressValidityMessage.setVisible(false);
    cityValidityMessage.setVisible(false);
    postcodeValidityMessage.setVisible(false);
    countryValidityMessage.setVisible(false);

    firstName = addTextField(leftPanel, "First Name", record.firstName, false, true);
    leftPanel.add(firstNameValidityMessage);
    secondName = addTextField(leftPanel, "Second Name", record.secondName, true, true);
    leftPanel.add(secondNameValidityMessage);
    sirname = addTextField(leftPanel, "Sirname", record.sirname, true, true);
    leftPanel.add(sirnameValidityMessage);
    String dayOfBirthString = "";
    String monthOfBirthString = "";
    String yearOfBirthString = "";
    if (record.dateOfBirth != null) {
      dayOfBirthString = Integer.toString(record.dateOfBirth.getDay());
      monthOfBirthString = Integer.toString(record.dateOfBirth.getMonth());
      yearOfBirthString = Integer.toString(record.dateOfBirth.getYear());
    }
    dayOfBirth = addTextField(leftPanel, "Day of Birth", dayOfBirthString, true, true);
    leftPanel.add(dayOfBirthValidityMessage);
    monthOfBirth = addTextField(leftPanel, "Month of Birth", monthOfBirthString, true, true);
    leftPanel.add(monthOfBirthValidityMessage);
    yearOfBirth = addTextField(leftPanel, "Year of Birth", yearOfBirthString, true, true);
    leftPanel.add(yearOfBirthValidityMessage);
    leftPanel.add(dateOfBirthValidityMessage);
    otherNotes = addTextField(leftPanel, "OtherNotes", record.otherNotes, true, true);
    leftPanel.add(otherNotesValidityMessage);
    email = addTextField(leftPanel, "Email", record.email, true, true);
    leftPanel.add(emailValidityMessage);
    phoneNumber = addTextField(leftPanel, "PhoneNumber", record.phoneNumber, true, true);
    leftPanel.add(phoneNumberValidityMessage);
    address = addTextField(leftPanel, "Address", record.address, true, true);
    leftPanel.add(addressValidityMessage);
    city = addTextField(leftPanel, "City", record.city, true, true);
    leftPanel.add(cityValidityMessage);
    postcode = addTextField(leftPanel, "Postcode", record.postcode, true, true);
    leftPanel.add(postcodeValidityMessage);
    country = addTextField(leftPanel, "Country", record.country, true, true);
    leftPanel.add(countryValidityMessage);
    genderList = addSelectList(leftPanel, "Gender", Gender.stringValues(), null);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Customer record, boolean editable) {
    if (record == null) {
      record = new Customer();
    }
    if (editable) {
      addRecordManagementFields(leftPanel, rightPanel, record);
      return;
    }
    
    addTextField(leftPanel, "Name", record.fullNameAndId(), false, editable);
    addTextField(leftPanel, "DateOfBirth", record.dateOfBirth.toString(), true, editable);
    addTextField(leftPanel, "OtherNotes", record.otherNotes, true, editable);
    addTextField(leftPanel, "Email", record.email, true, editable);
    addTextField(leftPanel, "PhoneNumber", record.phoneNumber, true, editable);
    addTextField(leftPanel, "Address", record.address, true, editable);
    addTextField(leftPanel, "City", record.city, true, editable);
    addTextField(leftPanel, "Postcode", record.postcode, true, editable);
    addTextField(leftPanel, "Country", record.country, true, editable);
    String gender = record.gender == null ? "" : record.gender.toString();
    addTextField(leftPanel, "Gender", gender, true, false);
  }
    
  protected Customer validateFormValues() {
    firstNameValidityMessage.setText("");
    firstNameValidityMessage.setVisible(false);
    secondNameValidityMessage.setText("");
    secondNameValidityMessage.setVisible(false);
    sirnameValidityMessage.setText("");
    sirnameValidityMessage.setVisible(false);
    dayOfBirthValidityMessage.setText("");
    dayOfBirthValidityMessage.setVisible(false);
    monthOfBirthValidityMessage.setText("");
    monthOfBirthValidityMessage.setVisible(false);
    yearOfBirthValidityMessage.setText("");
    yearOfBirthValidityMessage.setVisible(false);
    dateOfBirthValidityMessage.setText("");
    dateOfBirthValidityMessage.setVisible(false);
    otherNotesValidityMessage.setText("");
    otherNotesValidityMessage.setVisible(false);
    emailValidityMessage.setText("");
    emailValidityMessage.setVisible(false);
    phoneNumberValidityMessage.setText("");
    phoneNumberValidityMessage.setVisible(false);
    addressValidityMessage.setText("");
    addressValidityMessage.setVisible(false);
    cityValidityMessage.setText("");
    cityValidityMessage.setVisible(false);
    postcodeValidityMessage.setText("");
    postcodeValidityMessage.setVisible(false);
    countryValidityMessage.setText("");
    countryValidityMessage.setVisible(false);

    boolean formIsValid = true;
    Customer record = new Customer();

    // First Name
    record.firstName = firstName.getText();
    if (record.firstName.isBlank()) {
      firstNameValidityMessage.setText("First Name is required");
      firstNameValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.firstName.length() > 32) {
      firstNameValidityMessage.setText("First Name must be no more than 32 characters");
      firstNameValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Second Name
    record.secondName = secondName.getText();
    if (record.secondName.length() > 32) {
      secondNameValidityMessage.setText("Second Name must be no more than 32 characters");
      secondNameValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Sirname
    record.sirname = sirname.getText();
    if (record.sirname.length() > 32) {
      sirname.setText("Sirname must be no more than 32 characters");
      sirname.setVisible(true);
      formIsValid = false;
    }

    // Gender
    if (!genderList.isSelectionEmpty()) {
      record.gender = Gender.parseSelectedGender(genderList.getSelectedValue());
    }

    // Date of Birth
    int day = 0;
    int month = 0;
    int year = 0;
    try {
      if (!dayOfBirth.getText().isBlank())
        day = Integer.parseInt(dayOfBirth.getText());
    } catch (Exception e) {
      dayOfBirthValidityMessage.setText("Day of Birth must be a number");
      dayOfBirthValidityMessage.setVisible(true);
      formIsValid = false;
    }
    try {
      if (!monthOfBirth.getText().isBlank())
        month = Integer.parseInt(monthOfBirth.getText());
        month--;
    } catch (Exception e) {
      monthOfBirthValidityMessage.setText("Month of Birth must be a number");
      monthOfBirthValidityMessage.setVisible(true);
      formIsValid = false;
    }
    try {
      if (!yearOfBirth.getText().isBlank())
        year = Integer.parseInt(yearOfBirth.getText());
    } catch (Exception e) {
      yearOfBirthValidityMessage.setText("Year of Birth must be a number");
      yearOfBirthValidityMessage.setVisible(true);
      formIsValid = false;
    }
    record.dateOfBirth = new Date(year, month, day);
    boolean invalidYearGiven =
        record.dateOfBirth.getDate() != day ||
        record.dateOfBirth.getMonth() != month ||
        record.dateOfBirth.getYear() != year;
    boolean dateWasGiven =
        !yearOfBirth.getText().isBlank() ||
        !monthOfBirth.getText().isBlank() ||
        !dayOfBirth.getText().isBlank();
    if (dateWasGiven && invalidYearGiven) {
      dateOfBirthValidityMessage.setText("The date given is not valid");
      dateOfBirthValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Other Notes
    record.otherNotes = otherNotes.getText();

    // Email
    record.email = email.getText();
    if (record.email.length() > 128) {
      emailValidityMessage.setText("Email must be no more than 128 characters");
      emailValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Phone Number
    record.phoneNumber = phoneNumber.getText();
    if (record.phoneNumber.isBlank()) {
      phoneNumberValidityMessage.setText("Phone number is required");
      phoneNumberValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.phoneNumber.length() > 32) { // eTODO: regex validation
      phoneNumberValidityMessage.setText("Phone number must be no more than 32 characters");
      phoneNumberValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Address
    record.address = address.getText();
    if (record.address.length() > 32) { // eTODO: regex validation
      addressValidityMessage.setText("Address must be no more than 32 characters");
      addressValidityMessage.setVisible(true);
      formIsValid = false;
    }
    
    // City
    record.city = city.getText();
    if (record.city.length() > 32) { // eTODO: regex validation
      cityValidityMessage.setText("City must be no more than 32 characters");
      cityValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Postcode
    record.postcode = postcode.getText();
    if (record.city.length() > 32) { // eTODO: regex validation
      postcodeValidityMessage.setText("Postcode must be no more than 32 characters");
      postcodeValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Country
    record.country = country.getText();
    if (record.country.length() > 32) { // eTODO: regex validation
      countryValidityMessage.setText("Country must be no more than 32 characters");
      countryValidityMessage.setVisible(true);
      formIsValid = false;
    }

    if (formIsValid)
      return record;
    else
      return null;
  }
}
