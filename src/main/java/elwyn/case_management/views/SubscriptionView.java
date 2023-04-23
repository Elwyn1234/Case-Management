package elwyn.case_management.views;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Color;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Subscription;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import elwyn.case_management.models.Frequency;

public class SubscriptionView extends RecordView<Subscription> {
  JTextComponent description;
  JTextComponent name;
  JList<String> frequencyList;
  JTextComponent price;

  JLabel nameValidityMessage = new JLabel();
  JLabel descriptionValidityMessage = new JLabel();
  JLabel frequencyValidityMessage = new JLabel();
  JLabel priceValidityMessage = new JLabel();

  protected String pageTitle() { return "Subscriptions"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Subscriptions"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create Subscriptions"; }
  protected String tabNameOfEditRecord() { return "Edit Subscriptions"; }

  public SubscriptionView(RecordController<Subscription> controller) {
    super(controller, null);

    nameValidityMessage.setForeground(Color.RED);
    descriptionValidityMessage.setForeground(Color.RED);
    frequencyValidityMessage.setForeground(Color.RED);
    priceValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Subscription record) {
    if (record == null) {
      record = new Subscription();
    }
    nameValidityMessage.setVisible(false);
    descriptionValidityMessage.setVisible(false);
    frequencyValidityMessage.setVisible(false);
    priceValidityMessage.setVisible(false);

    name = addTextField(leftPanel, "name", record.name, false, true);
    leftPanel.add(nameValidityMessage);

    description = addTextField(leftPanel, "description", record.description, false, true);
    leftPanel.add(descriptionValidityMessage);

    String frequencyString = record.frequency == null ? null : record.frequency.toString();
    frequencyList = addSelectList(leftPanel, "Frequency", Frequency.stringValues(), frequencyString);
    leftPanel.add(frequencyValidityMessage);

    price = addTextField(leftPanel, "Price (pence)", Integer.toString(record.price), false, true); // eTODO: Default value should be an empty string, not 0
    leftPanel.add(priceValidityMessage);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Subscription record, boolean editable) {
    if (record == null) {
      record = new Subscription();
    }
    if (editable) {
      addRecordManagementFields(leftPanel, rightPanel, record);
      return;
    }

    LC lc = new LC();
    lc.setFlowX(false);
    MigLayout mig = new MigLayout(lc);
    leftPanel.setLayout(mig);

    JLabel titleLabel = new JLabel(record.name, SwingConstants.CENTER);
    titleLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, TITLE_SIZE));

    JTextArea descriptionArea = new JTextArea(record.description);
    descriptionArea.setPreferredSize(new Dimension(500, 120));
    descriptionArea.setEditable(false);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setLineWrap(true);
    descriptionArea.setMargin(new Insets(15, 0, 0, 0));

    String price = "£" + Integer.toString(record.price) + " " + record.frequency.toString();
    JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);
    priceLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, TITLE_SIZE));

    leftPanel.add(titleLabel, "align center");
    leftPanel.add(descriptionArea);
    leftPanel.add(priceLabel, "align center");
  }

  protected Subscription validateFormValues() {
    nameValidityMessage.setText("");
    nameValidityMessage.setVisible(false);
    descriptionValidityMessage.setText("");
    descriptionValidityMessage.setVisible(false);
    frequencyValidityMessage.setText("");
    frequencyValidityMessage.setVisible(false);
    priceValidityMessage.setText("");
    priceValidityMessage.setVisible(false);

    boolean formIsValid = true;
    Subscription record = new Subscription();

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
    
    // Frequency
    if (frequencyList.isSelectionEmpty()) {
      frequencyValidityMessage.setText("Frequency is required");
      frequencyValidityMessage.setVisible(true);
      formIsValid = false;
    }
    record.frequency = Frequency.parseSelectedFrequency(frequencyList.getSelectedValue()); //eTODO: rename parseSelectedX mthods


    // Price
    try {
      record.price = Integer.parseInt(price.getText()); // eTODO: handle exception
    } catch (Exception e) {
      priceValidityMessage.setText("Price is required and must be a whole number");
      priceValidityMessage.setVisible(true);
      formIsValid = false;
    }

    if (formIsValid)
      return record;
    else
      return null;
  }
}

