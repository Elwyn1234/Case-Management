package elwyn.case_management.views;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import elwyn.case_management.controllers.CustomerController;
import elwyn.case_management.controllers.HomeController;
import elwyn.case_management.controllers.LogController;
import elwyn.case_management.controllers.PerformanceController;
import elwyn.case_management.controllers.SubscriptionController;
import elwyn.case_management.controllers.UserController;
import elwyn.case_management.models.Role;
import elwyn.case_management.models.RouterModel;
import elwyn.case_management.models.User;
import elwyn.case_management.models.View;
import net.miginfocom.swing.MigLayout;
import elwyn.case_management.controllers.CaseController;
import elwyn.case_management.controllers.ContactController;

public class HomeView extends JTabbedPane {
  JFrame frame;
  HomeController homeController;
  Consumer<RouterModel> displayView;

  public HomeView(HomeController homeController, Consumer<RouterModel> displayView) {
    this.homeController = homeController;
    this.displayView = displayView;
    displayHome();
  }

  public void displayHome() {
    addTab("Home", createHomeView());

    CustomerView customerView = new CustomerView(new CustomerController(homeController.user));
    customerView.setViewportView(customerView.displayRecordListing());
    addTab("Customers", customerView);

    ContactView tabContactView = new ContactView(new ContactController(homeController.user, null));
    tabContactView.setViewportView(tabContactView.displayRecordListing());
    addTab("Contacts", tabContactView);

    CaseView tabCaseView = new CaseView(new CaseController(homeController.user, null));
    tabCaseView.setViewportView(tabCaseView.displayRecordListing());
    addTab("Cases", tabCaseView);

    SubscriptionView subscriptionView = new SubscriptionView(new SubscriptionController(homeController.user));
    subscriptionView.setViewportView(subscriptionView.displayRecordListing());
    addTab("Subscriptions", subscriptionView);

    { // My Performance View
      List<User> loggedInUser = new ArrayList<User>();
      loggedInUser.add(homeController.user);
      PerformanceController myPerformanceController = new PerformanceController(homeController.user);
      PerformanceView myPerformanceView = new PerformanceView(myPerformanceController, loggedInUser);
      myPerformanceView.display();
      addTab("My Performance", myPerformanceView);
    }

    if (homeController.user.role == Role.LEADER || homeController.user.role == Role.ADMIN) {
      UserController userController = new UserController(homeController.user);
      List<User> team = userController.readTeamMembers(homeController.user.id);
      PerformanceController teamPerformanceController = new PerformanceController(homeController.user);
      PerformanceView teamPerformanceView = new PerformanceView(teamPerformanceController, team);
      teamPerformanceView.display();
      addTab("Team's Performance", teamPerformanceView);
    }

    if (homeController.user.role == Role.ADMIN) {
      UserView userView = new UserView(new UserController(homeController.user));
      userView.setViewportView(userView.displayRecordListing());
      addTab("Users", userView);

      LogView logView = new LogView(new LogController(homeController.user));
      logView.setViewportView(logView.createLogDisplay());
      addTab("Logs", logView);
    }
  }

  JComponent createHomeView() {
    CaseController caseController = new CaseController(homeController.user, homeController.user::selectMyCases);
    ContactController contactController = new ContactController(homeController.user, homeController.user::selectMyContacts);

    JLabel titleLabel = new JLabel("Hello " + homeController.user.name);
    titleLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, 30));
    titleLabel.setBorder(new EmptyBorder(new Insets(30, 0, 0, 0)));

    JLabel caseCountLabel = new JLabel("You have " + caseController.readRecords(0).size() + " cases assigned to you");
    caseCountLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, 18));
    caseCountLabel.setBorder(new EmptyBorder(new Insets(30, 0, 0, 0)));

    JLabel contactCountLabel = new JLabel("You have " + contactController.readRecords(0).size() + " contacts assigned to you");
    contactCountLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, 18));
    contactCountLabel.setBorder(new EmptyBorder(new Insets(10, 0, 0, 0)));

    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          displayView.accept(new RouterModel(View.LOGIN, new User()));
        }
    });

    CaseView caseView = new CaseView(caseController);
    caseView.setViewportView(caseView.displayRecordListing());
    caseView.setBorder(new EmptyBorder(new Insets(30, 0, 0, 0)));

    ContactView contactView = new ContactView(contactController);
    contactView.setViewportView(contactView.displayRecordListing());
    contactView.setBorder(new EmptyBorder(new Insets(30, 0, 0, 0)));

    MigLayout mig = new MigLayout("wrap 1, alignx center");
    JPanel panel = new JPanel();
    panel.setLayout(mig);
    panel.add(titleLabel, "alignx center");
    panel.add(caseCountLabel, "alignx center"); // eTODO: fix pagination
    panel.add(contactCountLabel, "alignx center"); // eTODO: how many contacts in the last week?
    panel.add(Box.createVerticalStrut(10));
    panel.add(logoutButton, "alignx center");
    panel.add(caseView, "alignx center");
    panel.add(contactView, "alignx center");



    JScrollPane home = new JScrollPane();
    home.setViewportView(panel);
    home.addComponentListener(new ComponentListener() { // This must be done to handle databases changes that could happen in other tabs
        @Override
        public void componentShown(ComponentEvent e) {
          caseView.setViewportView(caseView.displayRecordListing());
          contactView.setViewportView(contactView.displayRecordListing()); //eTODO: refactor, displayX should display it, not return a panel
        }
        @Override
        public void componentResized(ComponentEvent e) {}
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
    });
    return home;
  }
}

//  eTODO:
//    Add notes to customer contacts
//    Cancel Subscriptions
//    display customer id in the customer listing
//    display a list of contacts under the cases listing
//    add a vip boolean to customer and add this field to the cases and contacts views as well
//    add status field to case records
//    Pull the User View into the new methodology
//    Change the address fields for customers
//    Validation
//    number fields should have a not set representation
//    Redefine how contacts and cases connect (not all contacts need a case) cases and contacts should have a User field
//    User should have a field defining their team leader
//    Handle User's names similar to that of customers
