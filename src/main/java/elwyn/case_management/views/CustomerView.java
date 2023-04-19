package elwyn.case_management.views;

import javax.swing.JList;
import javax.swing.text.JTextComponent;

import java.util.Date;

import javax.swing.JComponent;

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

  protected String pageTitle() { return "Customers"; }
  protected String tabNameOfViewRecords() { return "View Customers"; }
  protected String tabNameOfCreateRecord() { return "Create Customer"; }
  protected String tabNameOfEditRecord() { return "Edit Customer"; }

  public CustomerView(RecordController<Customer> controller) {
    super(controller, null);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Customer record, boolean editable) {
    if (record == null) {
      record = new Customer();
    }
    
    if (editable) {
      firstName = addTextField(leftPanel, "First Name", record.firstName, false, editable);
      secondName = addTextField(leftPanel, "Second Name", record.secondName, true, editable);
      sirname = addTextField(leftPanel, "Sirname", record.sirname, true, editable);
    } else {
      addTextField(leftPanel, "Name", record.fullNameAndId(), false, editable);
    }
    if (editable) {
      dayOfBirth = addTextField(leftPanel, "DateOfBirth", Integer.toString(record.dateOfBirth.getDate()), true, editable);
      monthOfBirth = addTextField(leftPanel, "DateOfBirth", Integer.toString(record.dateOfBirth.getMonth()), true, editable);
      yearOfBirth = addTextField(leftPanel, "DateOfBirth", Integer.toString(record.dateOfBirth.getYear()), true, editable);
    } else {
      addTextField(leftPanel, "DateOfBirth", record.dateOfBirth.toString(), true, editable);
    }
    otherNotes = addTextField(leftPanel, "OtherNotes", record.otherNotes, true, editable);
    email = addTextField(leftPanel, "Email", record.email, true, editable);
    phoneNumber = addTextField(leftPanel, "PhoneNumber", record.phoneNumber, true, editable);
    address = addTextField(leftPanel, "Address", record.address, true, editable);
    city = addTextField(leftPanel, "City", record.city, true, editable);
    postcode = addTextField(leftPanel, "Postcode", record.postcode, true, editable);
    country = addTextField(leftPanel, "Country", record.country, true, editable);

    String genderString = record.gender == null ? null : record.gender.toString();
    if (editable)
      genderList = addSelectList(leftPanel, "Gender", Gender.stringValues(), genderString);
    else
      addTextField(leftPanel, "Gender", genderString, true, false);
  }
    
  protected Customer getFormValues() {
    Customer record = new Customer();
    record.country = country.getText();
    record.postcode = postcode.getText();
    record.address = address.getText();
    record.city = city.getText();
    record.phoneNumber = phoneNumber.getText();
    record.email = email.getText();
    record.otherNotes = otherNotes.getText();
    int day = Integer.parseInt(dayOfBirth.getText());
    int month = Integer.parseInt(monthOfBirth.getText());
    int year = Integer.parseInt(yearOfBirth.getText());
    record.dateOfBirth = new Date(year, month, day);
    record.sirname = sirname.getText();
    record.secondName = secondName.getText();
    record.firstName = firstName.getText();
    record.gender = Gender.parseSelectedGender(genderList.getSelectedValue());
    return record;
  }
}
