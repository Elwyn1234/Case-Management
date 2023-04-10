package elwyn.case_management.views;

import javax.swing.JList;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Case;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.Contact;
import elwyn.case_management.models.ContactMethod;

public class ContactView extends RecordView<Contact> {
  JTextComponent description;
  JTextComponent date;
  JTextComponent time;
  JList<String> contactMethods;
  JTextComponent caseId;
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
    description = addTextField(panel, "Description", record.description, false, editable); //eTODO: rename from "Label" to "Field"
    date = addTextField(panel, "Date", record.date, false, editable);
    time = addTextField(panel, "Time", record.time, false, editable);
    String contactMethodString = record.contactMethod == null ? null : record.contactMethod.toString();
    if (editable)
      contactMethods = addSelectList(panel, "Contact Method", new String[] { "Phone", "Email", "Post", "Other" }, contactMethodString);
    else
      addTextField(panel, "Contact Method", contactMethodString, true, false);

    String caseId = record.caseRecord == null ? "" : Long.toString(record.caseRecord.id);
    this.caseId = addTextField(panel, "Case ID", caseId, false, editable); // eTODO: can we embed CaseView here
    if (!editable) {// Only want to see these fields in the Read view, not the Create or Update views
      addTextField(panel, "Case Summary", record.caseRecord.summary, false, editable);
      if (record.caseRecord.priority != null)
        addTextField(panel, "Case Priority", record.caseRecord.priority.toString(), false, editable); // eTODO: make this show

      Customer customer = record.caseRecord.customer;
      // if (customer != null) // eTODO: Add checks like this
      boolean hasSecondName = (customer.secondName != "" | customer.secondName != null);
      String fullName = customer.firstName + " " + (hasSecondName ? customer.secondName + " " : "") + customer.sirname;
      addTextField(panel, "Customer Name", fullName, false, editable);
      addTextField(panel, "Customer Email", customer.email, false, editable);
      addTextField(panel, "Customer Phone Number", customer.phoneNumber, false, editable);
    }
  }
    
  protected Contact getFormValues() {
    Contact record = new Contact();
    record.caseRecord = new Case();
    record.description = description.getText();
    record.date = date.getText();
    record.time = time.getText();
    record.caseRecord.id = Long.parseLong(caseId.getText()); // eTODO: handle exception


    String contactMethod = contactMethods.getSelectedValue();
    if (contactMethod != null)
      record.contactMethod = ContactMethod.parseSelectedContactMethod(contactMethod); //eTODO: rename parseSelectedX mthods
    return record;
  }
}
