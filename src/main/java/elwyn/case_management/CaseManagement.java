package elwyn.case_management;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import java.awt.event.*;

import elwyn.case_management.views.UserView;
import elwyn.case_management.views.CustomerView;
import elwyn.case_management.views.SubscriptionView;
import elwyn.case_management.views.CaseView;
import elwyn.case_management.views.ContactView;
import elwyn.case_management.controllers.CustomerController;
import elwyn.case_management.controllers.HomeController;
import elwyn.case_management.controllers.SubscriptionController;
import elwyn.case_management.controllers.UserController;
import elwyn.case_management.controllers.CaseController;
import elwyn.case_management.controllers.ContactController;

public class CaseManagement {
  public static void main(String[] args) {
    JTabbedPane tabbedPane = new JTabbedPane();
        
    CaseView caseView = new CaseView(new CaseController(HomeController::selectMyCases));
    ContactView contactView = new ContactView(new ContactController(HomeController::selectMyContacts));
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
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

    tabbedPane.addTab("Home", home);

    tabbedPane.addTab("Customers", new CustomerView(new CustomerController()));

    tabbedPane.addTab("Contacts", new ContactView(new ContactController(null)));
        
    tabbedPane.addTab("Cases", new CaseView(new CaseController(null)));

    tabbedPane.addTab("Subscriptions", new SubscriptionView(new SubscriptionController()));

    // JPanel performancePanel = new JPanel();
    // performancePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    // tabbedPane.addTab("Performance", performancePanel);

    tabbedPane.addTab("Users", new UserView(new UserController()));

    // JPanel loginPanel = new JPanel();
    // loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    // tabbedPane.addTab("Login", loginPanel);

    JFrame frame = new JFrame();
    frame.add(tabbedPane);
    frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}

//  eTODO:
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
