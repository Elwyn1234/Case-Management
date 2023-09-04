package elwyn.clinic.views;

import java.util.Date;

import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.text.JTextComponent;

import elwyn.clinic.controllers.AppointmentController;
import elwyn.clinic.models.Appointment;
import elwyn.clinic.models.Customer;
import elwyn.clinic.models.Role;
import elwyn.clinic.models.User;
import elwyn.clinic.models.VisitationStatus;
import net.miginfocom.swing.MigLayout;

public class AppointmentView extends RecordView<Appointment> {
  JTextComponent summary;
  JTextComponent description;
  JTextComponent customerId;
  JTextComponent referredTo;
  JList<String> visitationStatusList;

  JLabel summaryValidityMessage = new JLabel();
  JLabel customerIdValidityMessage = new JLabel();
  JLabel referredToValidityMessage = new JLabel();
  JLabel visitationStatusValidityMessage = new JLabel();

  AppointmentController appointmentController;

  protected String pageTitle() { return "Appointments"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Appointments"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create Appointment"; }
  protected String tabNameOfEditRecord() { return "Edit Appointment"; }



  public AppointmentView(AppointmentController controller) {
    super(controller);
    this.controller = controller;
    this.appointmentController = controller;

    if (controller.loggedInUser.role == Role.SP) {
      this.showDeleteButton = false;
      this.showCreateButton = false;
      this.showUpdateButton = false;
    }

    summaryValidityMessage.setForeground(Color.RED);
    customerIdValidityMessage.setForeground(Color.RED);
    referredToValidityMessage.setForeground(Color.RED);
    visitationStatusValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Appointment record) {
    if (record == null) {
      record = new Appointment();
    }
    summaryValidityMessage.setVisible(false);
    customerIdValidityMessage.setVisible(false);
    referredToValidityMessage.setVisible(false);
    visitationStatusValidityMessage.setVisible(false);

    String customerId = record.customer == null ? "" : Long.toString(record.customer.id);
    String referredTo = record.referredTo == null ? "" : Long.toString(record.referredTo.id);

    summary = addTextField(leftPanel, "Summary", record.summary, false, true);
    leftPanel.add(summaryValidityMessage);
    description = addTextArea(leftPanel, "Description", record.description, false, true);
    this.customerId = addTextField(leftPanel, "Customer ID", customerId, false, true); // eTODO: can we embed CustomerView here
    leftPanel.add(customerIdValidityMessage);
    this.referredTo = addTextField(leftPanel, "Assignee", referredTo, false, true);
    leftPanel.add(referredToValidityMessage);
    visitationStatusList = addSelectList(leftPanel, "Visitation Status", VisitationStatus.stringValues(), null);
    leftPanel.add(visitationStatusValidityMessage);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Appointment record, boolean editable) {
    if (record == null) {
      record = new Appointment();
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
    title.setFont(new Font(font, Font.PLAIN, 20));

    Box idBox = new Box(BoxLayout.X_AXIS);
    idBox = RecordView.createLabelledFieldInline("ID", Long.toString(record.id), font);

    Box creatorBox = new Box(BoxLayout.X_AXIS);
    if (record.createdBy != null)
      creatorBox = RecordView.createLabelledFieldInline("Creator", record.createdBy.fullNameAndId(), font);

    // eTODO: we should null check everything here in appointment something else in the future becomes optional
    Box assigneeBox = new Box(BoxLayout.X_AXIS);
    if (record.referredTo != null)
      assigneeBox = RecordView.createLabelledFieldInline("Assignee", record.referredTo.fullNameAndId(), font);

    Box dateBox = new Box(BoxLayout.X_AXIS);
    if (record.date != null)
      dateBox = RecordView.createLabelledFieldInline("", record.date.toString(), font);

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
    leftPanel.add(idBox, "cell 0 5");
    leftPanel.add(creatorBox, "cell 0 6");
    leftPanel.add(assigneeBox, "cell 0 7");
    leftPanel.add(dateBox, "cell 0 8");
    leftPanel.add(descriptionBox, "span");
  }

  protected Appointment validateFormValues() {
    summaryValidityMessage.setText("");
    summaryValidityMessage.setVisible(false);
    customerIdValidityMessage.setText("");
    customerIdValidityMessage.setVisible(false);
    referredToValidityMessage.setText("");
    referredToValidityMessage.setVisible(false);

    boolean formIsValid = true;
    Appointment record = new Appointment();

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
      record.customer = appointmentController.customerController.readRecord(Long.parseLong(customerId.getText()));
    if (record.customer == null) {
      customerIdValidityMessage.setText("Customer ID must be a valid Customer ID");
      customerIdValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Assignee
    record.referredTo = new User();
    if (!referredTo.getText().isBlank()) {
        record.referredTo = appointmentController.userController.readRecord(Long.parseLong(referredTo.getText()));
      if (record.referredTo == null) {
        referredToValidityMessage.setText("Assignee must be a valid User ID");
        referredToValidityMessage.setVisible(true);
        formIsValid = false;
      }
    }

    // Visitation Status
    if (!visitationStatusList.isSelectionEmpty()) {
      record.visitationStatus = VisitationStatus.parseSelectedStatus(visitationStatusList.getSelectedValue());
    }

    record.createdBy = appointmentController.loggedInUser;
    record.date = new Date();

    if (formIsValid)
      return record;
    else
      return null;
  }

  public static JPanel createAppointmentSummaryBox(Appointment record, String fontName) {
    JPanel panel = new JPanel(new MigLayout("wrap 1"));
    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    if (record == null) 
      return panel;

    JTextArea summaryArea = RecordView.createTextArea(record.summary);
    summaryArea.setPreferredSize(new Dimension(200, 50));

    JLabel appointmentSubsectionLabel = new JLabel("Appointment (" + record.id + ")");

    summaryArea.setFont(new Font(fontName, Font.PLAIN, 14));

    panel.add(appointmentSubsectionLabel);
    panel.add(summaryArea);
    return panel;
  }
}

