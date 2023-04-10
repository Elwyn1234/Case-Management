package elwyn.case_management.views;

import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.CaseController;
import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Case;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.Priority;
import elwyn.case_management.models.User;

public class CaseView extends RecordView<Case> {
  JTextComponent summary;
  JTextComponent description;
  JTextComponent customerId;
  JTextComponent userId;
  JTextComponent dateOpened;
  JTextComponent dateClosed;
  JList<String> priorityList;
  // Contacts List

  protected String tabNameOfViewRecords() { return "View Cases"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create Case"; }
  protected String tabNameOfEditRecord() { return "Edit Case"; }

  public CaseView(RecordController<Case> controller) {
    super(controller);
  }

  protected void addRecordFields(JComponent panel, Case record, boolean editable) {
    if (record == null) {
      record = new Case();
    }
    summary = addTextField(panel, "Summary", record.summary, false, editable); //eTODO: rename from "" to "Field"

    description = addTextArea(panel, "Description", record.description, false, editable);
    description.setPreferredSize(new Dimension(600, 400));

    String customerId = record.customer == null ? "" : Long.toString(record.customer.id);
    this.customerId = addTextField(panel, "Customer ID", customerId, false, editable); // eTODO: can we embed CustomerView here
    if (!editable) {// Only want to see these fields in the Read view, not the Create or Update views
      boolean hasSecondName = (record.customer.secondName != "" | record.customer.secondName != null);
      String fullName = record.customer.firstName + " " + (hasSecondName ? record.customer.secondName + " " : "") + record.customer.sirname;
      addTextField(panel, "Customer Name", fullName, false, editable);
      addTextField(panel, "Customer Email", record.customer.email, false, editable);
      addTextField(panel, "Customer Phone Number", record.customer.phoneNumber, false, editable);

      // eTODO: button \/
      // JButton commitButton = new JButton("View Customer Details");
      // commitButton.addActionListener(new ActionListener() {
      //     @Override
      //     public void actionPerformed(ActionEvent event) {
      //       record.id = customerId;
      //       setViewportView(createRecordsListPanel());
      //   }
      // });
      // panel.add(commitButton);
    }

    String userId = record.user == null ? "" : Long.toString(record.user.id);
    this.userId = addTextField(panel, "User ID", userId, false, editable); // eTODO: can we embed CustomerView here
    if (!editable) {// Only want to see these fields in the Read view, not the Create or Update views
      addTextField(panel, "User's Name", record.user.name, false, editable);
    }

    dateOpened = addTextField(panel, "Date Opened", record.dateOpened, false, editable);
    dateClosed = addTextField(panel, "Date Closed", record.dateClosed, false, editable);
    // if (!editable) {
    //   addTextField(panel, "Status", CaseController.caseClosed(record) ? "Closed" : "Open", editable, editable);
    //   if (CaseController.caseClosed(record)) {
    //     dateClosed = addTextField(panel, "Date Closed", record.dateClosed, false, editable);
    //   }
    // } else {
    //   dateClosed = addTextField(panel, "Date Closed", record.dateClosed, false, editable);
    // }
    String priorityString = record.priority == null ? null : record.priority.toString();
    if (editable)
      priorityList = addSelectList(panel, "Priority", Priority.stringValues(), priorityString);
    else
      addTextField(panel, "Priority", priorityString, true, false);
  }
    
  protected Case getFormValues() {
    Case record = new Case();
    record.customer = new Customer();
    record.user = new User();
    record.summary = summary.getText();
    record.description = description.getText();
    record.customer.id = Long.parseLong(customerId.getText()); // eTODO: handle exception
    record.user.id = Long.parseLong(userId.getText()); // eTODO: handle exception
    record.dateOpened = dateOpened.getText();
    record.dateClosed = dateClosed.getText();
    record.priority = Priority.parseSelectedPriority(priorityList.getSelectedValue()); //eTODO: rename parseSelectedX mthods
    return record;
  }
}

