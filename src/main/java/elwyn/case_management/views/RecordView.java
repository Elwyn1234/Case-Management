package elwyn.case_management.views;

import java.awt.Color;
import java.awt.event.*;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComponent;

import elwyn.case_management.models.Record;
import elwyn.case_management.controllers.RecordController;

public abstract class RecordView <T extends Record> extends JScrollPane {
  protected static final int TOP_MARGIN = 30;
  RecordController<T> controller;

  protected abstract String tabNameOfViewRecords();
  protected abstract String tabNameOfCreateRecord();
  protected abstract String tabNameOfEditRecord();
    
  public RecordView(RecordController<T> controller) {
    super();
    this.controller = controller;
    initComponents();
  }
    
  //*********** Component Initialisation ***********//

  void initComponents() {
    // panel.addComponentListener(new ComponentListener() {
    //     @Override
    //     public void componentShown(ComponentEvent e) {
    //       createRecordsListPanel(panel);
    //     }
    //     @Override
    //     public void componentResized(ComponentEvent e) {}
    //     @Override
    //     public void componentMoved(ComponentEvent e) {}
    //     @Override
    //     public void componentHidden(ComponentEvent e) {}
    // });

    setViewportView(createRecordsListPanel());
  }
  protected abstract void addRecordFields(JComponent panel, T record, boolean editable);
  
  protected JComponent createRecordsListPanel() {
    JComponent panel = new JPanel();
    panel.setSize(1028, 330);
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
    // panel.removeAll();

    JButton createButton = new JButton("Create");
    createButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          setViewportView(createRecordManagementPanel(null));
        }
    });
    panel.add(createButton);

    for (T record: controller.readRecords()) {
      JPanel leftFields = new JPanel();
      leftFields.setSize(300, 330);
      leftFields.setLayout(new BoxLayout(leftFields, BoxLayout.Y_AXIS));

      JPanel rightFields = new JPanel();
      rightFields.setSize(600, 330);
      rightFields.setLayout(new BoxLayout(rightFields, BoxLayout.Y_AXIS));

      addRecordFields(leftFields, record, false);

      JButton editButton = new JButton("Edit");
      editButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent event) {
            setViewportView(createRecordManagementPanel(record)); // eTODO: Rename to displayRecordManagementPanel
          }
      });
      panel.add(editButton);

      JButton deleteButton = new JButton("Delete");
      deleteButton.addActionListener(new ActionListener() {
          long recordId = record.id;
          @Override
          public void actionPerformed(ActionEvent event) {
            controller.deleteRecord(recordId); // eTODO: This is going to cause jumping
            setViewportView(createRecordsListPanel());
          }
      });
      panel.add(deleteButton);
      panel.add(leftFields);
      panel.add(rightFields);
    }        
    return panel;
  }

  protected JComponent createRecordManagementPanel(T record) {
    boolean createMode = record == null;

    JComponent panel = new JPanel();
    panel.setSize(1028, 330);
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JButton backButton = new JButton("Back");
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          setViewportView(createRecordsListPanel());
        }
    });
    panel.add(backButton);

    addRecordFields(panel, record, true);

    String buttonText = createMode ? "Create" : "Update";
    JButton commitButton = new JButton(buttonText);
    final long recordId = record == null ? 0 : record.id;
    commitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          T record = getFormValues();
          record.id = recordId;

          if (createMode)
            controller.createRecord(record);
          else
            controller.updateRecord(record);

          setViewportView(createRecordsListPanel());
      }
    });
    panel.add(commitButton);

    return panel;
  }
  protected abstract T getFormValues();
  


  //*********** Utilities ***********//

  protected static JTextField addTextField(JComponent panel, String labelText, String string, boolean hasTopMargin, boolean editable) {
    JLabel label = new JLabel(labelText);
    if (hasTopMargin)
      label.setBorder(BorderFactory.createEmptyBorder(TOP_MARGIN, 0, 0, 0));
    panel.add(label);
    JTextField textField = new JTextField(string);
    if (editable)
      panel.add(textField);
    else
      panel.add(new JLabel(string));
    return textField;
  }
  protected static JList<String> addSelectList(JComponent panel, String labelText, String[] strings, String defaultSelection) {
    JLabel label = new JLabel("labelText");
    label.setBorder(BorderFactory.createEmptyBorder(TOP_MARGIN, 0, 0, 0));
    panel.add(label);
    JList<String> list = new JList<String>(strings);

    boolean createMode = defaultSelection == null;
    if (!createMode) {
      list.setSelectedValue(defaultSelection, false);
    }
    panel.add(list);
    return list;
  }
}

// eTODO: static methods
