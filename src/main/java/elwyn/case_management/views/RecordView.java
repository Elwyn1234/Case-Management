package elwyn.case_management.views;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.*;

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
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import javax.swing.JComponent;

import elwyn.case_management.models.MiscButton;
import elwyn.case_management.models.Record;
import elwyn.case_management.models.Role;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import elwyn.case_management.controllers.RecordController;

public abstract class RecordView <T extends Record> extends JScrollPane {
  protected static final int TITLE_SIZE = 30;
  protected static final int TOP_MARGIN = 30;
  protected static final int LABEL_MARGIN = 10;
  protected RecordController<T> controller;
  protected MiscButton<T> miscButtonParams;
  int page = 0;
  JTextField pageJumpField;

  protected abstract String pageTitle();
  protected abstract String tabNameOfViewRecords();
  protected abstract String tabNameOfCreateRecord();
  protected abstract String tabNameOfEditRecord();
  protected String migLayoutString() { return "wrap 3, alignx center"; } 
    
  public RecordView(RecordController<T> controller) {
    super();
    this.controller = controller;

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
  }
    
  //*********** Component Initialisation ***********//

  protected abstract void addRecordFields(JComponent leftPanel, JComponent rightPanel, T record, boolean editable);
  
  public JComponent displayRecordListing() {
    JComponent panel = new JPanel();
    MigLayout migOuter = new MigLayout(migLayoutString());
    panel.setLayout(migOuter);

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

    panel.add(Box.createVerticalStrut(15), "span");
    panel.add(titleBox, "span, alignx center, aligny top");
    panel.add(Box.createVerticalStrut(15), "span");
    panel.add(mainButtons, "span, alignx center, aligny top");

    for (T record: controller.readRecords(page)) {
      Dimension expectedDimension = new Dimension(500, 200);
      MigLayout migInner = new MigLayout("wrap 1, aligny top");
      JPanel leftFields = new JPanel();
      Border innerBorder = BorderFactory.createEmptyBorder(10, 20, 10, 20);
      Border outerBorder = BorderFactory.createLineBorder(Color.BLACK, 1, true);
      leftFields.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
      leftFields.setLayout(migInner);
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
            controller.deleteRecord(recordId);
            setViewportView(displayRecordListing());
          }
      });

      JButton miscButton = null;
      if (miscButtonParams != null && miscButtonParams.shouldShowButton.apply(record)) {
        miscButton = new JButton(miscButtonParams.buttonText);
        miscButton.setPreferredSize(miscButtonParams.buttonDimension);
        miscButton.addActionListener(new ActionListener() {
            long recordId = record.id;
            @Override
            public void actionPerformed(ActionEvent event) {
              JComponent newDisplay = miscButtonParams.buttonPressed.apply(recordId);
              if (newDisplay == null) {
                setViewportView(displayRecordListing());
              } else  {
                setViewportView(newDisplay);
              }
            }
        });
      }
      JPanel buttons = new JPanel();
      buttons.add(editButton);
      Boolean showDeleteButton = controller.loggedInUser.role == Role.ADMIN;
      if (showDeleteButton) {
        buttons.add(Box.createHorizontalStrut(15));
        buttons.add(deleteButton);
      }
      buttons.add(Box.createHorizontalStrut(15));
      if (miscButton != null)
        buttons.add(miscButton);
      leftFields.add(buttons);
      panel.add(leftFields, "aligny top, grow");
    }

    return panel;
  }

  public JComponent displayRecordManagementPanel(T record) {
    boolean createMode = record == null;

    LC lc = new LC();
    lc.setWrapAfter(1);
    MigLayout mig = new MigLayout(lc);
    JComponent panel = new JPanel();
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    panel.setLayout(mig);
    // eTODO: sizing \/
    // Dimension expectedDimension = new Dimension(1000, 1000);
    // panel.setPreferredSize(expectedDimension);
    // panel.setMaximumSize(expectedDimension);
    // panel.setMinimumSize(expectedDimension);

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
    textComponent.setWrapStyleWord(true);
    textComponent.setLineWrap(true);
    return addTextComponent(panel, labelText, textComponent, hasTopMargin, editable);
  }

  public static JTextComponent addTextField(JComponent panel, String labelText, String string, boolean hasTopMargin, boolean editable) {
    JTextComponent textComponent = new JTextField(string);
    textComponent.setPreferredSize(new Dimension(100, 25));
    return addTextComponent(panel, labelText, textComponent, hasTopMargin, editable);
  }

  public static Box createLabelledTextArea(String label, String value, Insets insets) {
    JLabel descriptionLabel = new JLabel();
    if (label != null)
      descriptionLabel = new JLabel(label);
    JTextArea descriptionArea = new JTextArea(value);
    descriptionArea.setPreferredSize(new Dimension(400, 120));
    descriptionArea.setEditable(false);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setLineWrap(true);

    Box box = new Box(BoxLayout.Y_AXIS);
    box.setBorder(new EmptyBorder(insets));
    box.add(descriptionLabel);
    box.add(descriptionArea);
    return box;
  }

  public static JTextArea createTextArea(String value) {
    JTextArea title = new JTextArea(value);
    title.setEditable(false);
    title.setWrapStyleWord(true);
    title.setLineWrap(true);
    title.setOpaque(false);
    return title;
  }

  public static Box createLabelledFieldInline(String label, String value, String fontName) {
    Box box = new Box(BoxLayout.X_AXIS);
    JLabel a = new JLabel(label);
    JLabel b = new JLabel(value);
    b.setFont(new Font(fontName, Font.PLAIN, 14));
    box.add(a);
    box.add(Box.createHorizontalStrut(LABEL_MARGIN));
    box.add(b);
    return box;
  }

  public static JList<String> addSelectList(JComponent panel, String labelText, String[] strings, String defaultSelection) {
    JLabel label = new JLabel(labelText);
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
