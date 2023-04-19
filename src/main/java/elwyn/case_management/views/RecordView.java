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
import javax.swing.text.JTextComponent;
import javax.swing.JComponent;

import elwyn.case_management.models.MiscButton;
import elwyn.case_management.models.Record;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import elwyn.case_management.controllers.RecordController;

public abstract class RecordView <T extends Record> extends JScrollPane {
  protected static final int TITLE_SIZE = 30;
  protected static final int TOP_MARGIN = 30;
  protected static final int LABEL_MARGIN = 10;
  RecordController<T> controller;
  MiscButton<T> miscButtonParams;
  int page = 0;
  JTextField pageJumpField;

  protected abstract String pageTitle();
  protected abstract String tabNameOfViewRecords();
  protected abstract String tabNameOfCreateRecord();
  protected abstract String tabNameOfEditRecord();
    
  public RecordView(RecordController<T> controller, MiscButton<T> miscButtonParams) {
    super();
    this.controller = controller;
    this.miscButtonParams = miscButtonParams;
    initComponents();
  }
    
  //*********** Component Initialisation ***********//

  void initComponents() {
    setViewportView(displayRecordListing());
  }
  protected abstract void addRecordFields(JComponent leftPanel, JComponent rightPanel, T record, boolean editable);
  
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

    pageJumpField = new JTextField(Integer.toString(page));
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

    JLabel title = new JLabel(pageTitle(), SwingConstants.CENTER);
    title.setFont(new Font(getFont().getFontName(), Font.PLAIN, TITLE_SIZE));
    Box titleBox = new Box(BoxLayout.X_AXIS);
    titleBox.add(title);
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
    panel.add(titleBox);
    panel.add(Box.createVerticalStrut(15));
    panel.add(mainButtons);

    int i = 0;
    JPanel rowPanel = new JPanel();
    JPanel lastPanelAdded = new JPanel();
    for (T record: controller.readRecords(page)) {
      rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
      rowPanel.setAlignmentX(CENTER_ALIGNMENT);
      if (i % 3 == 0 && i != 0) {
        panel.add(rowPanel);
        lastPanelAdded = rowPanel;
        rowPanel = new JPanel();
      }
      Dimension expectedDimension = new Dimension(500, 630);
      LC lc = new LC();
      lc.setWrapAfter(1);
      MigLayout mig = new MigLayout(lc);
      JPanel leftFields = new JPanel();
      Border innerBorder = BorderFactory.createEmptyBorder(10, 20, 10, 20);
      Border outerBorder = BorderFactory.createLineBorder(Color.BLACK, 1, true);
      leftFields.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
      leftFields.setLayout(mig);
      leftFields.setPreferredSize(expectedDimension);
      leftFields.setMaximumSize(expectedDimension);
      leftFields.setMinimumSize(expectedDimension);

      JPanel rightFields = new JPanel();
      rightFields.setSize(600, 330);
      rightFields.setLayout(new BoxLayout(rightFields, BoxLayout.Y_AXIS));

      addRecordFields(leftFields, rightFields, record, false);

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

      JButton miscButton = null;
      if (miscButtonParams != null && miscButtonParams.shouldShowButton.apply(record)) {
        miscButton = new JButton(miscButtonParams.buttonText);
        miscButton.setPreferredSize(new Dimension(80, 30));
        miscButton.addActionListener(new ActionListener() {
            long recordId = record.id;
            @Override
            public void actionPerformed(ActionEvent event) {
              miscButtonParams.buttonPressed.accept(recordId); // eTODO: This is going to cause jumping
              setViewportView(displayRecordListing());
            }
        });
      }
      JPanel buttons = new JPanel();
      buttons.add(editButton);
      buttons.add(Box.createHorizontalStrut(15));
      buttons.add(deleteButton);
      buttons.add(Box.createHorizontalStrut(15));
      if (miscButton != null)
        buttons.add(miscButton);
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

    Dimension expectedDimension = new Dimension(1000, 1000);
    LC lc = new LC();
    lc.setWrapAfter(1);
    MigLayout mig = new MigLayout(lc);
    JComponent panel = new JPanel();
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    panel.setLayout(mig);
    panel.setPreferredSize(expectedDimension);
    panel.setMaximumSize(expectedDimension);
    panel.setMinimumSize(expectedDimension);

    JButton backButton = new JButton("Back");
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          setViewportView(displayRecordListing());
        }
    });
    panel.add(backButton);

    addRecordFields(panel, null, record, true); // eTODO: passing null bad

    String buttonText = createMode ? "Create" : "Update";
    JButton commitButton = new JButton(buttonText);
    final long recordId = record == null ? 0 : record.id;
    commitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          T record = validateFormValues();
          boolean formIsValid = record != null;
          if (!formIsValid)
            return;

          record.id = recordId;
          if (createMode)
            controller.createRecord(record);
          else
            controller.updateRecord(record);

          setViewportView(displayRecordListing());
      }
    });
    panel.add(commitButton);

    return centrePanel(panel);
  }
  protected abstract T validateFormValues();
  


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

  public static Box centrePanel(JComponent panel) {
    Box innerBox = new Box(BoxLayout.X_AXIS);
    innerBox.add(Box.createHorizontalGlue());
    innerBox.add(panel);
    innerBox.add(Box.createHorizontalGlue());

    Box outerBox = new Box(BoxLayout.Y_AXIS);
    outerBox.add(Box.createVerticalGlue());
    outerBox.add(innerBox);
    outerBox.add(Box.createVerticalGlue());

    return outerBox;
  }
}

// eTODO: static methods
