package elwyn.case_management.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.text.JTextComponent;
import javax.swing.JComponent;

import elwyn.case_management.models.Record;
import elwyn.case_management.controllers.RecordController;

public abstract class RecordView <T extends Record> extends JScrollPane {
  protected static final int TOP_MARGIN = 30;
  RecordController<T> controller;
  int page = 0;
  JTextField pageJumpField;

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
    setViewportView(displayRecordListing());
  }
  protected abstract void addRecordFields(JComponent panel, T record, boolean editable);
  
  public JComponent displayRecordListing() {
    JComponent panel = new JPanel();
    panel.setSize(1028, 630);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    // panel.removeAll();
    addComponentListener(new ComponentListener() { // This must be done to handle databases changes that could happen in other tabs
        @Override
        public void componentShown(ComponentEvent e) {
          setViewportView(displayRecordListing());
        }
        @Override
        public void componentResized(ComponentEvent e) {}
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
    });


    JButton previousButton = new JButton("Previous");
    if (page > 0) {
      previousButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent event) {
            page--;
            setViewportView(displayRecordListing());
          }
      });
    }

    pageJumpField = new JTextField();
    pageJumpField.setPreferredSize(new Dimension(50, 25));
    JButton jumpButton = new JButton("Jump");
    jumpButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          int newPage = Integer.parseInt(pageJumpField.getText()); // eTODO: validate string
          if (newPage >= 0 && newPage < controller.pageCount()) {
            page = newPage;
            setViewportView(displayRecordListing());
          }
        }
    });

    JButton nextButton = new JButton("Next");
    if (page + 1 < controller.pageCount()) {
      nextButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent event) {
            page++;
            setViewportView(displayRecordListing());
          }
      });
    }

    JButton createButton = new JButton("Create");
    createButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          setViewportView(displayRecordManagementPanel(null));
        }
    });

    JLabel title = new JLabel("Contacts", SwingConstants.CENTER);
    title.setFont(new Font(getFont().getFontName(), Font.PLAIN, 20));
    JPanel mainButtons = new JPanel();
    mainButtons.add(createButton);
    mainButtons.add(Box.createHorizontalStrut(15));
    mainButtons.add(previousButton);
    mainButtons.add(Box.createHorizontalStrut(15));
    mainButtons.add(pageJumpField);
    mainButtons.add(jumpButton);
    mainButtons.add(Box.createHorizontalStrut(15));
    mainButtons.add(nextButton);

    panel.add(Box.createVerticalStrut(15));
    panel.add(title);
    panel.add(Box.createVerticalStrut(15));
    panel.add(mainButtons);

    int i = 0;
    JPanel rowPanel = new JPanel();
    JPanel lastPanelAdded = new JPanel();
    for (T record: controller.readRecords(page)) {
      rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
      if (i % 3 == 0 && i != 0) {
        panel.add(rowPanel);
        lastPanelAdded = rowPanel;
        rowPanel = new JPanel();
      }
      JPanel leftFields = new JPanel();
      Border innerBorder = BorderFactory.createEmptyBorder(10, 20, 10, 20);
      Border outerBorder = BorderFactory.createLineBorder(Color.BLACK, 1, true);
      leftFields.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
      leftFields.setSize(500, 630);
      leftFields.setLayout(new BoxLayout(leftFields, BoxLayout.Y_AXIS));

      // JPanel rightFields = new JPanel();
      // rightFields.setSize(600, 330);
      // rightFields.setLayout(new BoxLayout(rightFields, BoxLayout.Y_AXIS));

      addRecordFields(leftFields, record, false);

      JButton editButton = new JButton("Edit");
      editButton.setPreferredSize(new Dimension(80, 30));
      editButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent event) {
            setViewportView(displayRecordManagementPanel(record)); // eTODO: Rename to displayRecordManagementPanel
          }
      });

      JButton deleteButton = new JButton("Delete");
      deleteButton.setPreferredSize(new Dimension(80, 30));
      deleteButton.addActionListener(new ActionListener() {
          long recordId = record.id;
          @Override
          public void actionPerformed(ActionEvent event) {
            controller.deleteRecord(recordId); // eTODO: This is going to cause jumping
            setViewportView(displayRecordListing());
          }
      });
      JPanel buttons = new JPanel();
      buttons.add(editButton);
      buttons.add(Box.createHorizontalStrut(15));
      buttons.add(deleteButton);
      leftFields.add(Box.createVerticalStrut(10));
      leftFields.add(buttons);
      rowPanel.add(leftFields);

      i++;
    }
    if (lastPanelAdded != rowPanel) {
      panel.add(rowPanel);
    }
    return panel;
  }

  public JComponent displayRecordManagementPanel(T record) {
    boolean createMode = record == null;

    JComponent panel = new JPanel();
    panel.setSize(1028, 330);
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JButton backButton = new JButton("Back");
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          setViewportView(displayRecordListing());
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
          if (!controller.isRecordValid(record))
          {
            // eTODO: user feedback
            return;
          }

          if (createMode)
            controller.createRecord(record);
          else
            controller.updateRecord(record);

          setViewportView(displayRecordListing());
      }
    });
    panel.add(commitButton);

    return panel;
  }
  protected abstract T getFormValues();
  


  //*********** Utilities ***********//

  private static JTextComponent addTextComponent(JComponent panel, String labelText, JTextComponent textComponent, boolean hasTopMargin, boolean editable) {
    JLabel label = new JLabel(labelText);
    if (hasTopMargin)
      label.setBorder(BorderFactory.createEmptyBorder(TOP_MARGIN, 0, 0, 0));
    panel.add(label);

    textComponent.setEditable(editable);
    panel.add(textComponent);
    return textComponent;
  }

  public static JTextComponent addTextArea(JComponent panel, String labelText, String string, boolean hasTopMargin, boolean editable) {
    JTextArea textComponent = new JTextArea(string);
    textComponent.setPreferredSize(new Dimension(400, 120));
    textComponent.setLineWrap(true);
    return addTextComponent(panel, labelText, textComponent, hasTopMargin, editable);
  }

  public static JTextComponent addTextField(JComponent panel, String labelText, String string, boolean hasTopMargin, boolean editable) {
    JTextComponent textComponent = new JTextField(string);
    textComponent.setPreferredSize(new Dimension(100, 25));
    return addTextComponent(panel, labelText, textComponent, hasTopMargin, editable);
  }

  public static JList<String> addSelectList(JComponent panel, String labelText, String[] strings, String defaultSelection) {
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
