package elwyn.case_management.views;

import java.awt.Color;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.LogController;
import elwyn.case_management.controllers.UserController;
import elwyn.case_management.models.Log;
import elwyn.case_management.models.Severity;
import elwyn.case_management.models.User;
import net.miginfocom.swing.MigLayout;

public class LogView extends JScrollPane {
  LogController logController;
  UserController userController;

  public LogView(LogController logController) {
    super();
    this.logController = logController;
    this.userController = new UserController(logController.loggedInUser);
  }

  protected JComponent createLogDisplay() {
    JPanel panel = new JPanel();
    MigLayout mig = new MigLayout("wrap 1, alignx center");
    panel.setLayout(mig);

    JLabel title = new JLabel("Logs");
    title.setFont(new Font(getFont().getFontName(), Font.PLAIN, 40));
    panel.add(title);
    panel.add(Box.createVerticalStrut(30));

    for (Log record : logController.readRecords(0)) {
      User user = userController.readRecord(record.user);

      JLabel name = null;
      if (user != null) {
        name = new JLabel(user.fullNameAndId());
        name.setFont(new Font(getFont().getFontName(), Font.BOLD, 30));
      }

      JLabel date = new JLabel(record.date.toString());
      date.setFont(new Font(getFont().getFontName(), Font.PLAIN, 20));

      JLabel severity = createSeverityLabel(record.severity, getFont().getFontName());

      JTextComponent log = RecordView.createTextArea(record.log);
      log.setPreferredSize(new Dimension(1000, 50));
      log.setFont(new Font(getFont().getFontName(), Font.PLAIN, 20));

      if (name != null)
        panel.add(name);
      panel.add(severity);
      panel.add(date);
      panel.add(log);
      panel.add(Box.createVerticalStrut(15));
    }

    addComponentListener(new ComponentListener() { // This must be done to handle databases changes that could happen in other tabs
        @Override
        public void componentShown(ComponentEvent e) {
          setViewportView(createLogDisplay());
        }
        @Override
        public void componentResized(ComponentEvent e) {}
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
    });

    return panel;
  }

  public static JLabel createSeverityLabel(Severity severity, String fontName) {
    JLabel label = new JLabel(severity.toString());
    label.setFont(new Font(fontName, Font.PLAIN, 20));
    if (severity == Severity.DEBUG)
      label.setForeground(new Color(0x00000088));
    if (severity == Severity.LOG)
      label.setForeground(new Color(0x00008800));
    if (severity == Severity.WARNING)
      label.setForeground(new Color(0x00B6C902));
    if (severity == Severity.ERROR)
      label.setForeground(new Color(0x00F59342));
    if (severity == Severity.FATAL)
      label.setForeground(Color.RED);
    return label;
  }
}

