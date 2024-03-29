package elwyn.clinic.views;

import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;
import javax.swing.JLabel;

import elwyn.clinic.models.Log;
import elwyn.clinic.models.RouterModel;
import elwyn.clinic.models.Severity;
import elwyn.clinic.models.User;
import elwyn.clinic.models.View;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import elwyn.clinic.controllers.LogController;
import elwyn.clinic.controllers.UserController;

public class LoginView extends JScrollPane {
  protected static final int TITLE_SIZE = 30;
  UserController userController;
  LogController logController;
  Consumer<RouterModel> displayView;
  JTextComponent usernameField;
  JTextComponent passwordField;

  JLabel credentialsValidityMessage = new JLabel();

  public LoginView(UserController userController, Consumer<RouterModel> displayView) {
    this.userController = userController;
    this.displayView = displayView;
    this.logController = new LogController(null);
    credentialsValidityMessage.setForeground(Color.RED);
    displayLogin();
  }

  public void displayLogin() {
    credentialsValidityMessage.setVisible(false);

    Dimension expectedDimension = new Dimension(250, 200);
    LC lc = new LC();
    lc.setWrapAfter(1);
    MigLayout mig = new MigLayout(lc);
    JPanel panel = new JPanel();
    panel.setLayout(mig);
    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    panel.setPreferredSize(expectedDimension);
    panel.setMaximumSize(expectedDimension);
    panel.setMinimumSize(expectedDimension);

    JLabel title = new JLabel("Login");
    title.setFont(new Font(getFont().getFontName(), Font.PLAIN, TITLE_SIZE));
    JPanel usernamePanel = new JPanel();
    JPanel passwordPanel = new JPanel();
    usernameField = RecordView.addTextField(usernamePanel, "Username", "", false, true);
    passwordField = RecordView.addTextField(passwordPanel, "Password", "", false, true);

    JButton loginButton = new JButton("Login");
    loginButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          credentialsValidityMessage.setText("");
          credentialsValidityMessage.setVisible(false);
          String username = usernameField.getText();
          String password = passwordField.getText();
          if (userController.areCredentialsValid(username, password)) {
            User user = userController.readRecord(username);

            Log log = new Log();
            log.severity = Severity.LOG;
            log.user = user.id;
            log.log = "User logged in successfully!";
            logController.createRecord(log);

            displayView.accept(new RouterModel(View.HOME, user));
          }
          else {
            credentialsValidityMessage.setText("Credentials are invalid");
            credentialsValidityMessage.setVisible(true);

            Log log = new Log();
            log.severity = Severity.LOG;
            log.log = "User failed to login with a username of " + username;
            logController.createRecord(log);
          }
        }
    });
    panel.add(RecordView.centrePanel(title));
    panel.add(RecordView.centrePanel(usernamePanel));
    panel.add(RecordView.centrePanel(passwordPanel));
    panel.add(credentialsValidityMessage);
    panel.add(RecordView.centrePanel(loginButton));

    setViewportView(RecordView.centrePanel(panel));


        // Dimension expectedDimension = new Dimension(100, 100);
        // JPanel paneltest = new JPanel();
        // paneltest.setPreferredSize(expectedDimension);
        // paneltest.setMaximumSize(expectedDimension);
        // paneltest.setMinimumSize(expectedDimension);
        // paneltest.setBackground(Color.RED); // for debug only

        // Box boxtest = new Box(BoxLayout.Y_AXIS);
        // boxtest.add(Box.createVerticalGlue());
        // boxtest.add(paneltest);     
        // boxtest.add(Box.createVerticalGlue());
        // setViewportView(boxtest);

    // User user = new User();
    // user.id = 1;
    // PerformanceController performanceController = new PerformanceController(user);
    // ContactController contactController = new ContactController(performanceController::selectMyContacts);
    // List<Date> contactDates = new ArrayList<Date>();
    // List<Integer> contactCounts = new ArrayList<Integer>();

    // Clock clock = Clock.tickMillis(ZoneId.systemDefault());
    // long start = clock.millis();

    // List<Contact> contacts = contactController.readRecords();

    // long end = clock.millis();
    // System.out.println("Read Time: " + Long.toString(end - start));



    // start = clock.millis();

    // // PerformanceView.quickSort(contacts, 0, contacts.size());

    // contacts.sort(new Comparator<Contact>() {
    //   public int compare(Contact o1, Contact o2) {
    //     int o1Value = o1.date.getYear() * 12 + o1.date.getMonth() * 31 + o1.date.getDate();
    //     int o2Value = o2.date.getYear() * 12 + o2.date.getMonth() * 31 + o2.date.getDate();
    //     return o1Value - o2Value;
    //   }
    // });

    // end = clock.millis();
    // System.out.println("Sort Time: " + Long.toString(end - start));



    // Date lastDate = contacts.get(0).date;
    // contactDates.add(lastDate);
    // contactCounts.add(1);

    // for (int i = 1; i < contacts.size(); i++) {
    //   if (PerformanceView.dateEquals(lastDate, contacts.get(i).date)) {
    //     int countsIndex = contactCounts.size() - 1;
    //     int count = contactCounts.get(countsIndex);
    //     contactCounts.set(countsIndex, ++count);
    //     continue;
    //   }
    //   lastDate = contacts.get(i).date;
    //   contactDates.add(lastDate);
    //   contactCounts.add(1);
    // }
    // for (int i = 0; i < contactDates.size(); i++) {
    //   System.out.println("Date: " + contactDates.get(i));
    //   System.out.println("Count: " + contactCounts.get(i));
    // }
  }
}

