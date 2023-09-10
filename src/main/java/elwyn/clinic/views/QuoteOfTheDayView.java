package elwyn.clinic.views;

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import elwyn.clinic.models.MiscButton;
import elwyn.clinic.models.Record;
import elwyn.clinic.controllers.RecordController;
import elwyn.clinic.controllers.UserController;
import elwyn.clinic.models.Role;
import elwyn.clinic.models.User;

public abstract class QuoteOfTheDayView extends JScrollPane {
  JTextComponent quoteOfTheDay;
  Connection conn;
  protected static final int TOP_MARGIN = 30;
  JTextComponent quoteBox;

  public QuoteOfTheDayView() {
    super();

    try { // eTODO: dependency injection
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:assets/clinic.db");
      if (conn == null) {
        System.out.println("ERROR: Your Database probably cant be found");
      }
    } catch (Exception ex) {
      // eTODO: log
      // Logger.getLogger(UserManagementView.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("ERROR: Your Database probably cant be found");
    }

    addComponentListener(new ComponentListener() { // This must be done to handle databases changes that could happen in other tabs
        @Override
        public void componentShown(ComponentEvent e) {
          setViewportView(displayManagementPanel());
        }
        @Override
        public void componentResized(ComponentEvent e) {}
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
    });
  }

  public JComponent displayManagementPanel() {
    Dimension expectedDimension = new Dimension(500, 200);
    MigLayout migInner = new MigLayout("wrap 1, aligny top");
    JPanel panel = new JPanel();
    Border innerBorder = BorderFactory.createEmptyBorder(10, 20, 10, 20);
    Border outerBorder = BorderFactory.createLineBorder(Color.BLACK, 1, true);
    panel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
    panel.setLayout(migInner);
    panel.setMinimumSize(expectedDimension);

    String quote = readQuote();
    quoteBox = addTextField(panel, "Quote of the Day", quote, false, true);

    JButton commitButton = new JButton("Update");
    commitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          try {
            String sql = "UPDATE quoteOfTheDay SET quote=? where quote IS NOT NULL;";
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, quote);
            pStatement.executeUpdate();
          } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
          }
      }
    });
    panel.add(commitButton);

    return panel;
  }

  public static JTextComponent addTextField(JComponent panel, String labelText, String string, boolean hasTopMargin, boolean editable) {
    JTextComponent textComponent = new JTextField(string);
    textComponent.setPreferredSize(new Dimension(100, 25));
    return addTextComponent(panel, labelText, textComponent, hasTopMargin, editable);
  }

  private static JTextComponent addTextComponent(JComponent panel, String labelText, JTextComponent textComponent, boolean hasTopMargin, boolean editable) {
    JLabel label = new JLabel(labelText);
    if (hasTopMargin)
      label.setBorder(BorderFactory.createEmptyBorder(TOP_MARGIN, 0, 0, 0));
    panel.add(label);

    textComponent.setEditable(editable);
    panel.add(textComponent);
    return textComponent;
  }

  public String readQuote() {
    String quote = "";
    try {
      String sql = "SELECT quote from quoteOfTheDay";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        quote = rs.getString("quote");
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return quote;
  }
}

