package elwyn.case_management;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import elwyn.case_management.views.UserView;
import elwyn.case_management.views.CustomerView;
import elwyn.case_management.views.SubscriptionView;
import elwyn.case_management.views.CaseView;
import elwyn.case_management.views.ContactView;
import elwyn.case_management.controllers.CustomerController;
import elwyn.case_management.controllers.SubscriptionController;
import elwyn.case_management.controllers.UserController;
import elwyn.case_management.controllers.CaseController;
import elwyn.case_management.controllers.ContactController;

public class CaseManagement {

    public static void main(String[] args) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // JScrollPane home = new JScrollPane();
        // JPanel panel = new JPanel();
        // panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // panel.add(new CaseView(new CaseController(new CustomerController())));
        // panel.add(new ContactView(new ContactController(new CaseController(new CustomerController()))));
        // home.setViewportView(panel);
        // tabbedPane.addTab("Home", home);

        tabbedPane.addTab("Customers", new CustomerView(new CustomerController()));

        CaseController caseController = new CaseController(new CustomerController(), new UserController());
        ContactController contactController = new ContactController(caseController, new UserController());
        tabbedPane.addTab("Contacts", new ContactView(contactController));
        
        caseController = new CaseController(new CustomerController(), new UserController());
        tabbedPane.addTab("Cases", new CaseView(caseController));

        tabbedPane.addTab("Subscriptions", new SubscriptionView(new SubscriptionController(new CustomerController())));

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
