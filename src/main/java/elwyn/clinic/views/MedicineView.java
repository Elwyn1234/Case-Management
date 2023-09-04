package elwyn.clinic.views;

import java.awt.Color;
import java.awt.Font;

import javax.swing.text.JTextComponent;
import javax.swing.JComponent;
import javax.swing.JLabel;

import elwyn.clinic.controllers.RecordController;
import elwyn.clinic.models.Medicine;
import net.miginfocom.swing.MigLayout;

public class MedicineView extends RecordView<Medicine> {
  JTextComponent name;
  JTextComponent description;

  JLabel nameValidityMessage = new JLabel();
  JLabel descriptionValidityMessage = new JLabel();
  
  protected String pageTitle() { return "Medicines"; }
  protected String tabNameOfViewRecords() { return "View Medicines"; }
  protected String tabNameOfCreateRecord() { return "Create Medicine"; }
  protected String tabNameOfEditRecord() { return "Edit Medicine"; }

  public MedicineView(RecordController<Medicine> controller) {
    super(controller);

    nameValidityMessage.setForeground(Color.RED);
    descriptionValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Medicine record) {
    nameValidityMessage.setVisible(false);
    descriptionValidityMessage.setVisible(false);

    name = addTextField(leftPanel, "Name", record.name, false, true);
    leftPanel.add(nameValidityMessage);
    description = addTextField(leftPanel, "Description", record.description, true, true);
    leftPanel.add(descriptionValidityMessage);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Medicine record, boolean editable) {
    if (record == null) {
      record = new Medicine();
    }
    if (editable) {
      addRecordManagementFields(leftPanel, rightPanel, record);
      return;
    }

    MigLayout mig = new MigLayout("wrap 2");
    leftPanel.setLayout(mig);
    String font = getFont().getFontName();

    JLabel nameLabel = new JLabel();
    if (record.name != null)
      nameLabel = new JLabel(record.name);
    nameLabel.setFont(new Font(font, Font.PLAIN, 14));

    JLabel descriptionLabel = new JLabel();
    if (record.description != null)
      descriptionLabel = new JLabel(record.description);
    descriptionLabel.setFont(new Font(font, Font.PLAIN, 14));

    leftPanel.add(nameLabel, "span, aligny top");
    leftPanel.add(descriptionLabel, "span, aligny top");
  }
    
  protected Medicine validateFormValues() {
    nameValidityMessage.setText("");
    nameValidityMessage.setVisible(false);
    descriptionValidityMessage.setText("");
    descriptionValidityMessage.setVisible(false);

    boolean formIsValid = true;
    Medicine record = new Medicine();

    // Name
    record.name = name.getText();
    if (record.name.isBlank()) {
      nameValidityMessage.setText("Name is required");
      nameValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.name.length() > 32) {
      nameValidityMessage.setText("Name must be no more than 32 characters");
      nameValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Description
    record.description = description.getText();
    if (record.description.length() > 32) {
      descriptionValidityMessage.setText("Description must be no more than 32 characters");
      descriptionValidityMessage.setVisible(true);
      formIsValid = false;
    }

    if (formIsValid)
      return record;
    else
      return null;
  }
}
