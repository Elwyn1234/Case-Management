package elwyn.mavenproject1;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Mavenproject1 {

    public static void main(String[] args) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel panel1 = new JPanel();
        tabbedPane.addTab("Home", panel1);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        new UsersView(panel2);
        tabbedPane.addTab("Roles", panel2);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Users", panel3);

        JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Customers", panel4);

        JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Contacts", panel5);

        JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Cases", panel6);

        JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Subscriptions", panel7);

        JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Performance", panel8);

        JPanel panel9 = new JPanel();
        panel9.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabbedPane.addTab("Login", panel9);

        JFrame frame = new JFrame();
        frame.add(tabbedPane);
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
