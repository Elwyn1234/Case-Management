package elwyn.case_management.views;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JComponent;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.Gender;

public class CustomerView extends RecordView<Customer> {
  JTextField firstNameLabel;
  JTextField secondNameLabel;
  JTextField sirnameLabel;
  JList<String> genderList;
  JTextField dateOfBirthLabel;
  JTextField otherNotesLabel;
  JTextField emailLabel;
  JTextField phoneNumberLabel;
  JTextField addressLine1Label;
  JTextField addressLine2Label;
  JTextField addressLine3Label;
  JTextField addressLine4Label;
  JTextField postcodeLabel;
  JTextField countryLabel;

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
    firstNameLabel = addTextField(panel, "First Name", record.firstName, false, editable);
    secondNameLabel = addTextField(panel, "Second Name", record.secondName, true, editable);
    sirnameLabel = addTextField(panel, "Sirname", record.sirname, true, editable);
    dateOfBirthLabel = addTextField(panel, "DateOfBirth", record.dateOfBirth, true, editable);
    otherNotesLabel = addTextField(panel, "OtherNotes", record.otherNotes, true, editable);
    emailLabel = addTextField(panel, "Email", record.email, true, editable);
    phoneNumberLabel = addTextField(panel, "PhoneNumber", record.phoneNumber, true, editable);
    addressLine1Label = addTextField(panel, "AddressLine1", record.addressLine1, true, editable);
    addressLine2Label = addTextField(panel, "AddressLine2", record.addressLine2, true, editable);
    addressLine3Label = addTextField(panel, "AddressLine3", record.addressLine3, true, editable);
    addressLine4Label = addTextField(panel, "AddressLine4", record.addressLine4, true, editable);
    postcodeLabel = addTextField(panel, "Postcode", record.postcode, true, editable);
    countryLabel = addTextField(panel, "Country", record.country, true, editable);

    String genderString = record.gender == null ? null : record.gender.toString();
    if (editable)
      genderList = addSelectList(panel, "Gender", new String[] { "NON_BINARY", "MALE", "FEMALE", "OTHER" }, genderString);
    else
      addTextField(panel, "Gender", genderString, true, false);
  }
    
  protected Customer getFormValues() {
    Customer record = new Customer();
    record.country = countryLabel.getText();
    record.postcode = postcodeLabel.getText();
    record.addressLine4 = addressLine4Label.getText();
    record.addressLine3 = addressLine3Label.getText();
    record.addressLine2 = addressLine2Label.getText();
    record.addressLine1 = addressLine1Label.getText();
    record.phoneNumber = phoneNumberLabel.getText();
    record.email = emailLabel.getText();
    record.otherNotes = otherNotesLabel.getText();
    record.dateOfBirth = dateOfBirthLabel.getText();
    record.sirname = sirnameLabel.getText();
    record.secondName = secondNameLabel.getText();
    record.firstName = firstNameLabel.getText();
    record.gender = Gender.parseSelectedGender(genderList.getSelectedValue());
    return record;
  }
}
