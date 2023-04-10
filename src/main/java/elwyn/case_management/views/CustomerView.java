package elwyn.case_management.views;

import javax.swing.JList;
import javax.swing.text.JTextComponent;
import javax.swing.JComponent;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.Gender;

public class CustomerView extends RecordView<Customer> {
  JTextComponent firstName;
  JTextComponent secondName;
  JTextComponent sirname;
  JList<String> genderList;
  JTextComponent dateOfBirth;
  JTextComponent otherNotes;
  JTextComponent email;
  JTextComponent phoneNumber;
  JTextComponent address;
  JTextComponent city;
  JTextComponent postcode;
  JTextComponent country;

  protected String tabNameOfViewRecords() { return "View Customers"; }
  protected String tabNameOfCreateRecord() { return "Create Customer"; }
  protected String tabNameOfEditRecord() { return "Edit Customer"; }

  public CustomerView(RecordController<Customer> controller) {
    super(controller);
  }

  protected void addRecordFields(JComponent panel, Customer record, boolean editable) {
    if (record == null) {
      record = new Customer();
    }
    firstName = addTextField(panel, "First Name", record.firstName, false, editable);
    secondName = addTextField(panel, "Second Name", record.secondName, true, editable);
    sirname = addTextField(panel, "Sirname", record.sirname, true, editable);
    dateOfBirth = addTextField(panel, "DateOfBirth", record.dateOfBirth, true, editable);
    otherNotes = addTextField(panel, "OtherNotes", record.otherNotes, true, editable);
    email = addTextField(panel, "Email", record.email, true, editable);
    phoneNumber = addTextField(panel, "PhoneNumber", record.phoneNumber, true, editable);
    address = addTextField(panel, "Address", record.address, true, editable);
    city = addTextField(panel, "City", record.city, true, editable);
    postcode = addTextField(panel, "Postcode", record.postcode, true, editable);
    country = addTextField(panel, "Country", record.country, true, editable);

    String genderString = record.gender == null ? null : record.gender.toString();
    if (editable)
      genderList = addSelectList(panel, "Gender", Gender.stringValues(), genderString);
    else
      addTextField(panel, "Gender", genderString, true, false);
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
    record.dateOfBirth = dateOfBirth.getText();
    record.sirname = sirname.getText();
    record.secondName = secondName.getText();
    record.firstName = firstName.getText();
    record.gender = Gender.parseSelectedGender(genderList.getSelectedValue());
    return record;
  }
}
