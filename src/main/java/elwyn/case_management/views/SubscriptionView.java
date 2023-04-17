package elwyn.case_management.views;

import javax.swing.JList;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Subscription;
import elwyn.case_management.models.SubscriptionType;
import elwyn.case_management.models.Customer;

public class SubscriptionView extends RecordView<Subscription> {
  JTextComponent customerId;
  JList<String> subscriptionTypes;
  JTextComponent dateStarted; //eTODO: set to the current date during creation
  JTextComponent days;

  protected String pageTitle() { return "Subscriptions"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Subscriptions"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create Subscriptions"; }
  protected String tabNameOfEditRecord() { return "Edit Subscriptions"; }

  public SubscriptionView(RecordController<Subscription> controller) {
    super(controller);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Subscription record, boolean editable) {
    if (record == null) {
      record = new Subscription();
    }
    String customerId = record.customer == null ? "" : Long.toString(record.customer.id);
    this.customerId = addTextField(leftPanel, "Customer ID", customerId, false, editable); // eTODO: can we embed CustomerView here

    String subscriptionType = record.subscriptionType == null ? null : record.subscriptionType.toString();
    if (editable)
      subscriptionTypes = addSelectList(leftPanel, "Subscription Type", SubscriptionType.stringValues(), subscriptionType);
    else
      addTextField(leftPanel, "Priority", subscriptionType, true, false);

    if (!editable) {// Only want to see these fields in the Read view, not the Create or Update views
      boolean hasSecondName = (record.customer.secondName != "" | record.customer.secondName != null);
      String fullName = record.customer.firstName + " " + (hasSecondName ? record.customer.secondName + " " : "") + record.customer.sirname;
      addTextField(leftPanel, "Customer Name", fullName, false, editable);
      addTextField(leftPanel, "Customer Email", record.customer.email, false, editable);
      addTextField(leftPanel, "Customer Phone Number", record.customer.phoneNumber, false, editable);

      // eTODO: button \/
      // JButton commitButton = new JButton("View Customer Details");
      // commitButton.addActionListener(new ActionListener() {
      //     @Override
      //     public void actionPerformed(ActionEvent event) {
      //       record.id = customerId;
      //       setViewportView(createRecordsListPanel());
      //   }
      // });
      // leftPanel.add(commitButton);
    }
    dateStarted = addTextField(leftPanel, "Date Started", record.dateStarted, false, editable);
    days = addTextField(leftPanel, "Days", Long.toString(record.days), false, editable); // eTODO: generate how many days till the subscription runs out
  } // eTODO: generate the subscriptions status. has it ended
    
  protected Subscription getFormValues() {
    Subscription record = new Subscription();
    record.customer = new Customer();
    record.customer.id = Long.parseLong(customerId.getText()); // eTODO: handle exception
    record.subscriptionType = SubscriptionType.parseSelectedSubscriptionType(subscriptionTypes.getSelectedValue());
    record.dateStarted = dateStarted.getText();
    record.days = Integer.parseInt(days.getText()); // eTODO: handle exception
    return record;
  }
}

