package elwyn.case_management.views;

import java.awt.Color;

import javax.swing.JList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.CaseController;
import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.controllers.UserController;
import elwyn.case_management.models.Case;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.User;
import elwyn.case_management.models.Contact;
import elwyn.case_management.models.ContactMethod;

public class ContactView extends RecordView<Contact> {
  JTextComponent description;
  JList<String> contactMethods;
  JTextComponent caseId;
  JTextComponent userId;

  JLabel descriptionValidityMessage = new JLabel();
  JLabel contactMethodsValidityMessage = new JLabel();
  JLabel caseIdValidityMessage = new JLabel();
  JLabel userIdValidityMessage = new JLabel();

  protected String pageTitle() { return "Contacts"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Contact"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create Contact"; }
  protected String tabNameOfEditRecord() { return "Edit Contact"; }

  public ContactView(RecordController<Contact> controller) {
    super(controller, null);

    descriptionValidityMessage.setVisible(false);
    contactMethodsValidityMessage.setVisible(false);
    caseIdValidityMessage.setVisible(false);
    userIdValidityMessage.setVisible(false);
    descriptionValidityMessage.setForeground(Color.RED);
    contactMethodsValidityMessage.setForeground(Color.RED);
    caseIdValidityMessage.setForeground(Color.RED);
    userIdValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Contact record) {
    description = addTextArea(leftPanel, "Description", record.description, false, true);
    leftPanel.add(descriptionValidityMessage);

    String contactMethodString = record.contactMethod == null ? null : record.contactMethod.toString();
    contactMethods = addSelectList(leftPanel, "Contact Method", ContactMethod.stringValues(), contactMethodString);
    leftPanel.add(contactMethodsValidityMessage);

    this.caseId = addTextField(leftPanel, "Case ID", "", true, true);
    leftPanel.add(caseIdValidityMessage);

    this.userId = addTextField(leftPanel, "User ID", Long.toString(controller.loggedInUser.id), true, true); // eTODO: can we embed CustomerView here
    leftPanel.add(userIdValidityMessage);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Contact record, boolean editable) {
    if (record == null) {
      record = new Contact();
    }
    if (editable) {
      addRecordManagementFields(leftPanel, rightPanel, record);
      return;
    }

    description = addTextArea(leftPanel, "Description", record.description, false, editable);

    addTextField(leftPanel, "Date", record.date.toString(), false, editable);

    String contactMethodString = record.contactMethod == null ? null : record.contactMethod.toString();
    addTextField(leftPanel, "Contact Method", contactMethodString, false, false);

    String caseId = record.caseRecord == null ? "" : Long.toString(record.caseRecord.id);
    this.caseId = addTextField(leftPanel, "Case ID", caseId, true, editable); // eTODO: can we embed CaseView here
    if (record.caseRecord != null) {
      addTextField(leftPanel, "Case Summary", record.caseRecord.summary, false, editable);
      if (record.caseRecord.priority != null)
        addTextField(leftPanel, "Case Priority", record.caseRecord.priority.toString(), false, editable); // eTODO: make this show
  
      Customer customer = record.caseRecord.customer;
      if (customer != null) { // eTODO: Add checks like this (especially important to deal with cases or customers that are deleted)
        boolean hasSecondName = (customer.secondName != "" | customer.secondName != null);
        String fullName = customer.firstName + " " + (hasSecondName ? customer.secondName + " " : "") + customer.sirname;
        addTextField(leftPanel, "Customer Name", fullName, true, editable);
        addTextField(leftPanel, "Customer Email", customer.email, false, editable);
        addTextField(leftPanel, "Customer Phone Number", customer.phoneNumber, false, editable);
      }
    }

    String userId = record.user == null ? "" : Long.toString(record.user.id);
    this.userId = addTextField(leftPanel, "User ID", userId, true, editable); // eTODO: can we embed CustomerView here
    addTextField(leftPanel, "User's Name", record.user.name, false, editable);
  }
    
  protected Contact validateFormValues() {
    descriptionValidityMessage.setText("");
    descriptionValidityMessage.setVisible(false);
    contactMethodsValidityMessage.setText("");
    contactMethodsValidityMessage.setVisible(false);
    caseIdValidityMessage.setText("");
    caseIdValidityMessage.setVisible(false);
    userIdValidityMessage.setText("");
    userIdValidityMessage.setVisible(false);

    CaseController caseController = new CaseController(controller.loggedInUser, null);
    UserController userController = new UserController(controller.loggedInUser);
    boolean formIsValid = true;
    Contact record = new Contact();

    // Description
    record.description = description.getText();
    if (record.description.isBlank()) {
      descriptionValidityMessage.setText("Description is required");
      descriptionValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Case
    record.caseRecord = new Case();
    if (!caseId.getText().isBlank()) {
      try {
        record.caseRecord.id = Long.parseLong(caseId.getText()); // eTODO: handle exception
      } catch (Exception e) {
        caseIdValidityMessage.setText("Case ID must be a valid Case ID");
        caseIdValidityMessage.setVisible(true);
        formIsValid = false;
      }
      Case caseRecord = caseController.readRecord(record.caseRecord.id);
      if (caseRecord == null) {
        caseIdValidityMessage.setText("Case ID must be a valid Case ID");
        caseIdValidityMessage.setVisible(true);
        formIsValid = false;
      }
    }

    // User
    record.user = new User();
    try {
      record.user.id = Long.parseLong(userId.getText()); // eTODO: handle exception
    } catch (Exception e) {
      userIdValidityMessage.setText("User ID must be a valid User ID");
      userIdValidityMessage.setVisible(true);
      formIsValid = false;
    }
    User user = userController.readRecord(record.user.id);
    if (user == null) {
      userIdValidityMessage.setText("User ID must be a valid User ID");
      userIdValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Contact
    if (contactMethods.isSelectionEmpty()) {
      contactMethodsValidityMessage.setText("Contact Method is required");
      contactMethodsValidityMessage.setVisible(true);
      formIsValid = false;
    }
    record.contactMethod = ContactMethod.parseSelectedContactMethod(contactMethods.getSelectedValue()); //eTODO: rename parseSelectedX mthods

    if (formIsValid)
      return record;
    else
      return null;
  }
}

