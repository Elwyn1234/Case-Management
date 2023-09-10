package elwyn.clinic.views;

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

import elwyn.clinic.controllers.DiseaseController;
import elwyn.clinic.models.Disease;
import elwyn.clinic.models.Category;
import net.miginfocom.swing.MigLayout;

public class DiseaseView extends RecordView<Disease> {
  JTextComponent name;
  JTextComponent description;
  JList<String> categoryList;

  JLabel nameValidityMessage = new JLabel();
  JLabel categoryValidityMessage = new JLabel();

  DiseaseController diseaseController;
  // Contacts List

  protected String pageTitle() { return "Diseases"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Diseases"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create Disease"; }
  protected String tabNameOfEditRecord() { return "Edit Disease"; }



  public DiseaseView(DiseaseController controller) {
    super(controller);
    this.controller = controller;
    this.diseaseController = controller;

    nameValidityMessage.setForeground(Color.RED);
    categoryValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Disease record) {
    if (record == null) {
      record = new Disease();
    }
    nameValidityMessage.setVisible(false);
    categoryValidityMessage.setVisible(false);

    String categoryString = record.category == null ? null : record.category.toString();

    name = addTextField(leftPanel, "Name", record.name, false, true);
    leftPanel.add(nameValidityMessage);
    description = addTextArea(leftPanel, "Description", record.description, false, true);
    categoryList = addSelectList(leftPanel, "Category", Category.stringValues(), categoryString);
    leftPanel.add(categoryValidityMessage);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Disease record, boolean editable) {
    if (record == null) {
      record = new Disease();
    }
    if (editable) {
      addRecordManagementFields(leftPanel, rightPanel, record);
      return;
    }

    String font = getFont().getFontName();
    MigLayout mig = new MigLayout("wrap 2");
    leftPanel.setLayout(mig);

    JTextArea title = RecordView.createTextArea(record.name);
    title.setPreferredSize(new Dimension(400, 50));
    title.setFont(new Font(font, Font.PLAIN, 20));

    Box idBox = new Box(BoxLayout.X_AXIS);
    idBox = RecordView.createLabelledFieldInline("ID", Long.toString(record.id), font);

    JLabel categoryLabel = new JLabel("Category");
    Box categoryBox = new Box(BoxLayout.X_AXIS);
    categoryBox.add(categoryLabel);
    categoryBox.add(Box.createHorizontalStrut(LABEL_MARGIN));
    categoryBox.add(DiseaseView.createCategoryLabel(record.category, font));

    Box descriptionBox = RecordView.createLabelledTextArea("Description", record.description, new Insets(TOP_MARGIN, 0, 0, 0));

    leftPanel.add(title, "span, aligny top");
    leftPanel.add(categoryBox, "span, aligny top");
    leftPanel.add(idBox, "span, aligny top");
    leftPanel.add(descriptionBox, "span, aligny top");
  }

  protected Disease validateFormValues() {
    nameValidityMessage.setText("");
    nameValidityMessage.setVisible(false);
    categoryValidityMessage.setText("");
    categoryValidityMessage.setVisible(false);

    boolean formIsValid = true;
    Disease record = new Disease();

    // Name
    record.name = name.getText();
    if (record.name.isBlank()) {
      nameValidityMessage.setText("Name is required");
      nameValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.name.length() > 80) {
      nameValidityMessage.setText("Name must be no more than 80 characters");
      nameValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Description
    record.description = description.getText();
    
    // Category
    if (categoryList.isSelectionEmpty()) {
      categoryValidityMessage.setText("Category is required");
      categoryValidityMessage.setVisible(true);
      formIsValid = false;
    }
    record.category = Category.parseSelectedCategory(categoryList.getSelectedValue()); //eTODO: rename parseSelectedX mthods

    if (formIsValid)
      return record;
    else
      return null;
  }

  public static JPanel createDiseaseNameBox(Disease record, String fontName) {
    JPanel panel = new JPanel(new MigLayout("wrap 1"));
    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    if (record == null) 
      return panel;

    JTextArea nameArea = RecordView.createTextArea(record.name);
    nameArea.setPreferredSize(new Dimension(200, 50));

    JLabel diseaseSubsectionLabel = new JLabel("Disease (" + record.id + ")");

    nameArea.setFont(new Font(fontName, Font.PLAIN, 14));

    panel.add(diseaseSubsectionLabel);
    panel.add(nameArea);
    panel.add(createCategoryLabel(record.category, fontName));
    return panel;
  }

  public static JLabel createCategoryLabel(Category category, String fontName) {
    JLabel label = new JLabel(category.toString());
    label.setFont(new Font(fontName, Font.PLAIN, 15));
    return label;
  }
}

