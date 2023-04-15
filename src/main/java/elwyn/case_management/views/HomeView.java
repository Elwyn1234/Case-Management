package elwyn.case_management.views;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;

import java.awt.event.*;
import java.util.function.Consumer;

import elwyn.case_management.controllers.CustomerController;
import elwyn.case_management.controllers.HomeController;
import elwyn.case_management.controllers.PerformanceController;
import elwyn.case_management.controllers.SubscriptionController;
import elwyn.case_management.controllers.UserController;
import elwyn.case_management.models.RouterModel;
import elwyn.case_management.models.User;
import elwyn.case_management.models.View;
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
    CaseController caseController = new CaseController(homeController::selectMyCases);
    ContactController contactController = new ContactController(homeController::selectMyContacts);
    CaseView caseView = new CaseView(caseController);
    ContactView contactView = new ContactView(contactController);
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    panel.add(new JLabel("Hello " + homeController.user.name));
    panel.add(new JLabel("You have " + caseController.readRecords(0).size() + " cases assigned to you")); // eTODO: fix pagination
    panel.add(new JLabel("You have " + contactController.readRecords(0).size() + " contacts assigned to you")); // eTODO: how many contacts in the last week?
    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          displayView.accept(new RouterModel(View.LOGIN, new User()));
        }
    });
    panel.add(logoutButton);
    panel.add(caseView);
    panel.add(contactView);

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

    addTab("Home", home);

    addTab("Customers", new CustomerView(new CustomerController()));

    addTab("Contacts", new ContactView(new ContactController(null)));
        
    addTab("Cases", new CaseView(new CaseController(null)));

    addTab("Subscriptions", new SubscriptionView(new SubscriptionController()));

    PerformanceController performanceController = new PerformanceController(homeController.user);
    addTab("Team's Performance", new PerformanceView(performanceController));

    // addTab("My Performance", performancePanel);

    // if (homeController.user.role == Role.ADMIN)
      addTab("Users", new UserView(new UserController()));
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
