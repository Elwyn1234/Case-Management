package elwyn.case_management.views;

import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
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
    super(controller, new MiscButton<Case>(controller::closeRecord, controller::shouldShowButton, "Close"));
    this.controller = controller;
    this.caseController = controller;

    summaryValidityMessage.setVisible(false);
    customerIdValidityMessage.setVisible(false);
    assignedToValidityMessage.setVisible(false);
    priorityValidityMessage.setVisible(false);
    summaryValidityMessage.setForeground(Color.RED);
    customerIdValidityMessage.setForeground(Color.RED);
    assignedToValidityMessage.setForeground(Color.RED);
    priorityValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Case record) {
    if (record == null) {
      record = new Case();
    }
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

    MigLayout mig = new MigLayout("wrap 2");
    leftPanel.setLayout(mig);

    JLabel titleLabel = new JLabel(record.summary, SwingConstants.CENTER);
    titleLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, TITLE_SIZE));

    JComponent leftInnerPanel = new JPanel();
    JComponent rightInnerPanel = new JPanel();
    leftInnerPanel.setLayout(new BoxLayout(leftInnerPanel, BoxLayout.Y_AXIS));
    rightInnerPanel.setLayout(new BoxLayout(rightInnerPanel, BoxLayout.Y_AXIS));


    JLabel creatorLabel = new JLabel("Creator");
    JLabel creatorValue = new JLabel(record.createdBy.fullNameAndId() + " on " + record.dateOpened);
    creatorValue.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
    Box creatorBox = new Box(BoxLayout.X_AXIS);
    creatorBox.add(creatorLabel);
    creatorBox.add(Box.createHorizontalStrut(LABEL_MARGIN));
    creatorBox.add(creatorValue);

    Box assigneeBox = new Box(BoxLayout.X_AXIS);
    if (record.assignedTo != null) {
      JLabel assigneeLabel = new JLabel("Assignee");
      JLabel assigneeValue = new JLabel(record.assignedTo.fullNameAndId());
      assigneeValue.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
      assigneeBox.add(assigneeLabel);
      assigneeBox.add(Box.createHorizontalStrut(LABEL_MARGIN));
      assigneeBox.add(assigneeValue);
    } // eTODO: we should null check everything here in case something else in the future becomes optional

    JLabel statusLabel = new JLabel("Status");
    JLabel statusValue = new JLabel(record.getStatus());
    statusValue.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
    Box statusBox = new Box(BoxLayout.X_AXIS);
    statusBox.add(statusLabel);
    statusBox.add(Box.createHorizontalStrut(LABEL_MARGIN));
    statusBox.add(statusValue);

    JLabel priorityLabel = new JLabel("Priority");
    JLabel priorityValue = new JLabel(record.priority.toString());
    priorityValue.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
    Box priorityBox = new Box(BoxLayout.X_AXIS);
    priorityBox.add(priorityLabel);
    priorityBox.add(Box.createHorizontalStrut(LABEL_MARGIN));
    priorityBox.add(priorityValue);



    JLabel customerSubsectionLabel = new JLabel("Customer");
    String openedForText = record.customer.fullNameAndId();
    JLabel openedForLabel = new JLabel(openedForText, SwingConstants.RIGHT);
    openedForLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
    JLabel customerEmailLabel = new JLabel(record.customer.email);
    customerEmailLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
    JLabel customerPhoneNumberLabel = new JLabel(record.customer.phoneNumber);
    customerPhoneNumberLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
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

    JLabel descriptionLabel = new JLabel("Description");
    descriptionLabel.setBorder(new EmptyBorder(new Insets(TOP_MARGIN, 0, 0, 0)));
    JTextArea descriptionArea = new JTextArea(record.description);
    descriptionArea.setPreferredSize(new Dimension(500, 120));
    descriptionArea.setEditable(false);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setLineWrap(true);
    descriptionArea.setMargin(new Insets(15, 0, 0, 0));


    // leftInnerPanel.add(customerSubsectionLabel);
    // leftInnerPanel.add(openedForLabel);
    // leftInnerPanel.add(customerEmailLabel);
    // leftInnerPanel.add(customerPhoneNumberLabel);
    // leftInnerPanel.add(creatorBox);
    // leftInnerPanel.add(assigneeBox);
    // rightInnerPanel.add(statusBox);
    // rightInnerPanel.add(priorityBox);

    leftPanel.add(titleLabel, "span 2");
    leftPanel.add(customerSubsectionLabel);
    leftPanel.add(statusBox, "align right");
    leftPanel.add(openedForLabel);
    leftPanel.add(priorityBox, "align right");
    leftPanel.add(customerEmailLabel, "span 2");
    leftPanel.add(customerPhoneNumberLabel, "span 2");
    leftPanel.add(creatorBox, "span 2");
    leftPanel.add(assigneeBox, "span 2");
    leftPanel.add(descriptionLabel, "span 2");
    leftPanel.add(descriptionArea, "span 2");
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
    }
    Customer customer = caseController.customerController.readRecord(record.customer.id);
    if (customer == null) {
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
      }
      User user = caseController.userController.readRecord(record.assignedTo.id);
      if (user == null) {
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
}

