package elwyn.case_management;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import elwyn.case_management.views.CustomerView;
import elwyn.case_management.views.UserView;
import elwyn.case_management.controllers.CustomerController;

public class CaseManagement {

    public static void main(String[] args) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel homePanel = new JPanel();
        tabbedPane.addTab("Home", homePanel);

        JPanel rolesPanel = new JPanel();
        rolesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Roles", rolesPanel);

        tabbedPane.addTab("Customers", new CustomerView(new CustomerController()));

        JPanel contactsPanel = new JPanel();
        contactsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Contacts", contactsPanel);

        JPanel casesPanel = new JPanel();
        casesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Cases", casesPanel);

        JPanel subscriptionsPanel = new JPanel();
        subscriptionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Subscriptions", subscriptionsPanel);

        JPanel performancePanel = new JPanel();
        performancePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Performance", performancePanel);

        JPanel userManagementPanel = new JPanel();
        userManagementPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        new UserView(userManagementPanel);
        tabbedPane.addTab("User Management", userManagementPanel);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Login", loginPanel);

        JFrame frame = new JFrame();
        frame.add(tabbedPane);
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
