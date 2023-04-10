package elwyn.case_management;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import elwyn.case_management.views.UserView;
import elwyn.case_management.views.CustomerView;
import elwyn.case_management.views.SubscriptionView;
import elwyn.case_management.views.CaseView;
import elwyn.case_management.views.ContactView;
import elwyn.case_management.controllers.CustomerController;
import elwyn.case_management.controllers.SubscriptionController;
import elwyn.case_management.controllers.CaseController;
import elwyn.case_management.controllers.ContactController;

public class CaseManagement {

    public static void main(String[] args) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel homePanel = new JPanel();
        tabbedPane.addTab("Home", homePanel);

        tabbedPane.addTab("Customers", new CustomerView(new CustomerController()));

        tabbedPane.addTab("Contacts", new ContactView(new ContactController(new CaseController(new CustomerController()))));

        tabbedPane.addTab("Cases", new CaseView(new CaseController(new CustomerController())));

        tabbedPane.addTab("Subscriptions", new SubscriptionView(new SubscriptionController(new CustomerController())));

        // JPanel performancePanel = new JPanel();
        // performancePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        // tabbedPane.addTab("Performance", performancePanel);

        JPanel userManagementPanel = new JPanel();
        userManagementPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        new UserView(userManagementPanel);
        tabbedPane.addTab("User Management", userManagementPanel);

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
//    fix enum CRUD actions
//    display customer id in the customer listing
//    display a list of contacts under the cases listing
//    add a vip boolean to customer and add this field to the cases and contacts views as well
//    add status field to case records
//    Pull the User View into the new methodology
//    Change the address fields for customers
//    Validation
