package elwyn.case_management.views;

import java.awt.FlowLayout;
import java.awt.Insets;
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
import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Case;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.MiscButton;
import elwyn.case_management.models.Priority;
import elwyn.case_management.models.User;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

public class CaseView extends RecordView<Case> {
  JTextComponent summary;
  JTextComponent description;
  JTextComponent customerId;
  JTextComponent assignedTo;
  JTextComponent dayOpened;
  JTextComponent monthOpened;
  JTextComponent yearOpened;
  JTextComponent secondOpened;
  JTextComponent minuteOpened;
  JTextComponent hourOpened;
  JTextComponent dayClosed;
  JTextComponent monthClosed;
  JTextComponent yearClosed;
  JTextComponent secondClosed;
  JTextComponent minuteClosed;
  JTextComponent hourClosed;
  JTextComponent dateClosed;
  JList<String> priorityList;
  CaseController caseController;
  // Contacts List

  protected String pageTitle() { return "Cases"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Cases"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create Case"; }
  protected String tabNameOfEditRecord() { return "Edit Case"; }

  public CaseView(CaseController controller) {
    super(controller, new MiscButton<Case>(controller::closeRecord, controller::shouldShowButton, "Close"));
    this.controller = controller;
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Case record) {
    if (record == null) {
      record = new Case();
    }
    String customerId = record.customer == null ? "" : Long.toString(record.customer.id);
    String assignedTo = record.assignedTo == null ? "" : Long.toString(record.assignedTo.id);
    String priorityString = record.priority == null ? null : record.priority.toString();

    summary = addTextField(leftPanel, "Summary", record.summary, false, true);
    description = addTextArea(leftPanel, "Description", record.description, false, true);
    this.customerId = addTextField(leftPanel, "Customer ID", customerId, false, true); // eTODO: can we embed CustomerView here
    this.assignedTo = addTextField(leftPanel, "Assignee", assignedTo, false, true);
    // eTODO: Autofill all date time fields
    // dayClosed = addTextField(leftPanel, "Day Closed", Integer.toString(record.dateClosed.getDate()), false, true);
    // monthClosed = addTextField(leftPanel, "Month Closed", Integer.toString(record.dateClosed.getMonth()), false, true);
    // yearClosed = addTextField(leftPanel, "Year Closed", Integer.toString(record.dateClosed.getYear()), false, true);
    // secondClosed = addTextField(leftPanel, "Second Closed", Integer.toString(record.dateClosed.getSeconds()), false, true);
    // minuteClosed = addTextField(leftPanel, "Minute Closed", Integer.toString(record.dateClosed.getMinutes()), false, true);
    // hourClosed = addTextField(leftPanel, "Hour Closed", Integer.toString(record.dateClosed.getHours()), false, true);
    priorityList = addSelectList(leftPanel, "Priority", Priority.stringValues(), priorityString);
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

    JLabel assigneeLabel = new JLabel("Assignee");
    JLabel assigneeValue = new JLabel(record.assignedTo.fullNameAndId());
    assigneeValue.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
    Box assigneeBox = new Box(BoxLayout.X_AXIS);
    assigneeBox.add(assigneeLabel);
    assigneeBox.add(Box.createHorizontalStrut(LABEL_MARGIN));
    assigneeBox.add(assigneeValue);

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
    
  protected Case getFormValues() {
    Case record = new Case();
    record.customer = new Customer();
    record.assignedTo = new User();
    record.summary = summary.getText();
    record.description = description.getText();
    record.customer.id = Long.parseLong(customerId.getText()); // eTODO: handle exception
    record.assignedTo.id = Long.parseLong(assignedTo.getText()); // eTODO: handle exception
    record.priority = Priority.parseSelectedPriority(priorityList.getSelectedValue()); //eTODO: rename parseSelectedX mthods
    return record;
  }
}

