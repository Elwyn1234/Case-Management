package elwyn.case_management.views;

import javax.swing.JList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Case;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.User;
import elwyn.case_management.models.Contact;
import elwyn.case_management.models.ContactMethod;

public class ContactView extends RecordView<Contact> {
  JTextComponent description;
  JTextComponent date;
  JTextComponent time;
  JList<String> contactMethods;
  JTextComponent caseId;
  JTextComponent userId;
  // Contacts List

  protected String tabNameOfViewRecords() { return "View Contact"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create Contact"; }
  protected String tabNameOfEditRecord() { return "Edit Contact"; }

  public ContactView(RecordController<Contact> controller) {
    super(controller);
  }

  protected void addRecordFields(JComponent panel, Contact record, boolean editable) {
    if (record == null) {
      record = new Contact();
    }
    description = addTextArea(panel, "Description", record.description, false, editable);
    date = addTextField(panel, "Date", record.date.toString(), false, editable);
    time = addTextField(panel, "Time", record.time, false, editable);
    String contactMethodString = record.contactMethod == null ? null : record.contactMethod.toString();
    if (editable)
      contactMethods = addSelectList(panel, "Contact Method", ContactMethod.stringValues(), contactMethodString);
    else
      addTextField(panel, "Contact Method", contactMethodString, false, false);

    String caseId = record.caseRecord == null ? "" : Long.toString(record.caseRecord.id);
    this.caseId = addTextField(panel, "Case ID", caseId, true, editable); // eTODO: can we embed CaseView here
    if (!editable) {// Only want to see these fields in the Read view, not the Create or Update views
      if (record.caseRecord != null) {
        addTextField(panel, "Case Summary", record.caseRecord.summary, false, editable);
        if (record.caseRecord.priority != null)
          addTextField(panel, "Case Priority", record.caseRecord.priority.toString(), false, editable); // eTODO: make this show
  
        Customer customer = record.caseRecord.customer;
        if (customer != null) { // eTODO: Add checks like this (especially important to deal with cases or customers that are deleted)
          boolean hasSecondName = (customer.secondName != "" | customer.secondName != null);
          String fullName = customer.firstName + " " + (hasSecondName ? customer.secondName + " " : "") + customer.sirname;
          addTextField(panel, "Customer Name", fullName, true, editable);
          addTextField(panel, "Customer Email", customer.email, false, editable);
          addTextField(panel, "Customer Phone Number", customer.phoneNumber, false, editable);
        }
      }
    }

    String userId = record.user == null ? "" : Long.toString(record.user.id);
    this.userId = addTextField(panel, "User ID", userId, true, editable); // eTODO: can we embed CustomerView here
    if (!editable) {// Only want to see these fields in the Read view, not the Create or Update views
      addTextField(panel, "User's Name", record.user.name, false, editable);
    }
  }
    
  protected Contact getFormValues() {
    Contact record = new Contact();
    record.caseRecord = new Case();
    record.user = new User();
    record.description = description.getText();
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    try {
      record.date = df.parse(date.getText());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    record.time = time.getText();
    record.caseRecord.id = Long.parseLong(caseId.getText()); // eTODO: handle exception
    record.user.id = Long.parseLong(userId.getText()); // eTODO: handle exception
    record.contactMethod = ContactMethod.parseSelectedContactMethod(contactMethods.getSelectedValue()); //eTODO: rename parseSelectedX mthods
    return record;
  }
}

