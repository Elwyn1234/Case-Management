package elwyn.case_management.views;

import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.CaseController;
import elwyn.case_management.models.Case;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.MiscButton;
import elwyn.case_management.models.Priority;
import elwyn.case_management.models.User;
import net.miginfocom.swing.MigLayout;

public class CaseView extends RecordView<Case> {
  JTextComponent summary;
  JTextComponent description;
  JTextComponent customerId;
  JTextComponent assignedTo;
  JList<String> priorityList;

  JLabel summaryValidityMessage = new JLabel();
  JLabel customerIdValidityMessage = new JLabel();
  JLabel assignedToValidityMessage = new JLabel();
  JLabel priorityValidityMessage = new JLabel();

  CaseController caseController;
  // Contacts List

  protected String pageTitle() { return "Cases"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Cases"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create Case"; }
  protected String tabNameOfEditRecord() { return "Edit Case"; }



  public CaseView(CaseController controller) {
    super(controller);
    this.controller = controller;
    this.caseController = controller;
    miscButtonParams = new MiscButton<Case>(controller::closeRecord, controller::shouldShowButton, "Close");

    summaryValidityMessage.setForeground(Color.RED);
    customerIdValidityMessage.setForeground(Color.RED);
    assignedToValidityMessage.setForeground(Color.RED);
    priorityValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Case record) {
    if (record == null) {
      record = new Case();
    }
    summaryValidityMessage.setVisible(false);
    customerIdValidityMessage.setVisible(false);
    assignedToValidityMessage.setVisible(false);
    priorityValidityMessage.setVisible(false);

    String customerId = record.customer == null ? "" : Long.toString(record.customer.id);
    String assignedTo = record.assignedTo == null ? "" : Long.toString(record.assignedTo.id);
    String priorityString = record.priority == null ? null : record.priority.toString();

    summary = addTextField(leftPanel, "Summary", record.summary, false, true);
    leftPanel.add(summaryValidityMessage);
    description = addTextArea(leftPanel, "Description", record.description, false, true);
    this.customerId = addTextField(leftPanel, "Customer ID", customerId, false, true); // eTODO: can we embed CustomerView here
    leftPanel.add(customerIdValidityMessage);
    this.assignedTo = addTextField(leftPanel, "Assignee", assignedTo, false, true);
    leftPanel.add(assignedToValidityMessage);
    priorityList = addSelectList(leftPanel, "Priority", Priority.stringValues(), priorityString);
    leftPanel.add(priorityValidityMessage);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Case record, boolean editable) {
    if (record == null) {
      record = new Case();
    }
    if (editable) {
      addRecordManagementFields(leftPanel, rightPanel, record);
      return;
    }

    String font = getFont().getFontName();
    MigLayout mig = new MigLayout("wrap 2");
    leftPanel.setLayout(mig);

    JTextArea title = RecordView.createTextArea(record.summary);
    title.setPreferredSize(new Dimension(400, 50));
    title.setFont(new Font(font, Font.PLAIN, TITLE_SIZE));

    Box creatorBox = new Box(BoxLayout.X_AXIS);
    if (record.createdBy != null)
      creatorBox = RecordView.createLabelledFieldInline("Creator", record.createdBy.fullNameAndId(), font);

    // eTODO: we should null check everything here in case something else in the future becomes optional
    Box assigneeBox = new Box(BoxLayout.X_AXIS);
    if (record.assignedTo != null)
      assigneeBox = RecordView.createLabelledFieldInline("Assignee", record.assignedTo.fullNameAndId(), font);

    Box dateOpenedBox = new Box(BoxLayout.X_AXIS);
    if (record.dateOpened != null)
      dateOpenedBox = RecordView.createLabelledFieldInline("Opened", record.dateOpened.toString(), font);

    Box dateClosedBox = new Box(BoxLayout.X_AXIS);
    if (record.dateClosed != null)
      dateClosedBox = RecordView.createLabelledFieldInline("Closed", record.dateClosed.toString(), font);

    Box statusBox = new Box(BoxLayout.X_AXIS);
    statusBox = RecordView.createLabelledFieldInline("Status", record.getStatus(), font);

    JLabel priorityLabel = new JLabel("Priority");
    Box priorityBox = new Box(BoxLayout.X_AXIS);
    priorityBox.add(priorityLabel);
    priorityBox.add(Box.createHorizontalStrut(LABEL_MARGIN));
    priorityBox.add(CaseView.createPriorityLabel(record.priority, font));

    JPanel customerBox = CustomerView.createCustomerSummaryBox(record.customer, font, true);
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

    Box descriptionBox = RecordView.createLabelledTextArea("Description", record.description, new Insets(TOP_MARGIN, 0, 0, 0));

    leftPanel.add(title, "span 2");
    leftPanel.add(customerBox, "cell 0 1 1 4");
    leftPanel.add(statusBox, "cell 1 1, align right");
    leftPanel.add(priorityBox, "cell 1 2, align right");
    leftPanel.add(creatorBox, "cell 0 5");
    leftPanel.add(assigneeBox, "cell 0 6");
    leftPanel.add(dateOpenedBox, "cell 0 7");
    leftPanel.add(dateClosedBox, "wrap, cell 0 8");
    leftPanel.add(descriptionBox, "span");
  }

  protected Case validateFormValues() {
    summaryValidityMessage.setText("");
    summaryValidityMessage.setVisible(false);
    customerIdValidityMessage.setText("");
    customerIdValidityMessage.setVisible(false);
    assignedToValidityMessage.setText("");
    assignedToValidityMessage.setVisible(false);
    priorityValidityMessage.setText("");
    priorityValidityMessage.setVisible(false);

    boolean formIsValid = true;
    Case record = new Case();

    // Summary
    record.summary = summary.getText();
    if (record.summary.isBlank()) {
      summaryValidityMessage.setText("Summary is required");
      summaryValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.summary.length() > 80) {
      summaryValidityMessage.setText("Summary must be no more than 80 characters");
      summaryValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Description
    record.description = description.getText();
    
    // Customer
    record.customer = new Customer();
    try {
      record.customer.id = Long.parseLong(customerId.getText()); // eTODO: handle exception
    } catch (Exception e) {
      customerIdValidityMessage.setText("Customer ID must be a valid Customer ID");
      customerIdValidityMessage.setVisible(true);
      formIsValid = false;
      record.customer = null;
    }
    record.customer = caseController.customerController.readRecord(record.customer.id);
    if (record.customer == null) {
      customerIdValidityMessage.setText("Customer ID must be a valid Customer ID");
      customerIdValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Assignee
    record.assignedTo = new User();
    if (!assignedTo.getText().isBlank()) {
      try {
        record.assignedTo.id = Long.parseLong(assignedTo.getText()); // eTODO: handle exception
      } catch (Exception e) {
        assignedToValidityMessage.setText("Assignee must be a valid User ID");
        assignedToValidityMessage.setVisible(true);
        formIsValid = false;
        record.assignedTo = null;
      }
      record.assignedTo = caseController.userController.readRecord(record.assignedTo.id);
      if (record.assignedTo == null) {
        assignedToValidityMessage.setText("Assignee must be a valid User ID");
        assignedToValidityMessage.setVisible(true);
        formIsValid = false;
      }
    }

    // Priority
    if (priorityList.isSelectionEmpty()) {
      priorityValidityMessage.setText("Priority is required");
      priorityValidityMessage.setVisible(true);
      formIsValid = false;
    }
    record.priority = Priority.parseSelectedPriority(priorityList.getSelectedValue()); //eTODO: rename parseSelectedX mthods

    if (formIsValid)
      return record;
    else
      return null;
  }

  public static JPanel createCaseSummaryBox(Case record, String fontName) {
    JPanel panel = new JPanel(new MigLayout("wrap 1"));
    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    if (record == null) 
      return panel;

    JTextArea summaryArea = RecordView.createTextArea(record.summary);
    summaryArea.setPreferredSize(new Dimension(200, 50));

    JLabel caseSubsectionLabel = new JLabel("Case (" + record.id + ")");
    JLabel caseStatusLabel = new JLabel(record.getStatus());

    summaryArea.setFont(new Font(fontName, Font.PLAIN, 14));
    caseStatusLabel.setFont(new Font(fontName, Font.PLAIN, 14));

    panel.add(caseSubsectionLabel);
    panel.add(summaryArea);
    panel.add(createPriorityLabel(record.priority, fontName));
    panel.add(caseStatusLabel);
    return panel;
  }

  public static JLabel createPriorityLabel(Priority priority, String fontName) {
    JLabel label = new JLabel(priority.toString());
    label.setFont(new Font(fontName, Font.PLAIN, 15));
    if (priority == Priority.URGENT)
      label.setForeground(Color.RED);
    if (priority == Priority.HIGH)
      label.setForeground(new Color(0x00F59342));
    if (priority == Priority.MEDIUM)
      label.setForeground(new Color(0x00B6C902));
    if (priority == Priority.LOW)
      label.setForeground(new Color(0x00008800));
    return label;
  }
}

