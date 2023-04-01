package elwyn.mavenproject1;

import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UsersView {
    List<User> users = new ArrayList<User>();
    JPanel displayPanel;
    Connection conn;
    
    public UsersView(JPanel displayPanel) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:assets/caseManagement.db");
        } catch (Exception ex) {
            Logger.getLogger(UsersView.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.displayPanel = displayPanel;
        loadUsers();
        initComponents();
    }
    
    public void loadUsers() {
        try {
            String sql = "SELECT rowid, * from users";
            PreparedStatement pStatement = conn.prepareStatement(sql);
            ResultSet rs = pStatement.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.id = rs.getInt("rowid");
                user.name = rs.getString("name");
                user.username = rs.getString("username");
                user.password = rs.getString("password");
                user.role = rs.getString("role");
                users.add(user);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        filterUsers();
    }
    
    public void filterUsers() {}
    
    public void addExtraLabels(JPanel panel, User user) {}
    
    public void initComponents() {
//        JButton createButton = new JButton();
//        createButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("hi from button");
//            }
//        });
//        displayPanel.add(createButton);
        
        for (var user : users) {
            JPanel item = new JPanel();
            item.setSize(1028, 330);
            item.setBorder(BorderFactory.createLineBorder(Color.black));
            item.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
            
            JPanel leftFields = new JPanel();
            leftFields.setSize(300, 330);
            leftFields.setLayout(new BoxLayout(leftFields, BoxLayout.Y_AXIS));
            
            JPanel rightFields = new JPanel();
            rightFields.setSize(600, 330);
            rightFields.setLayout(new BoxLayout(rightFields, BoxLayout.Y_AXIS));

            
            
            leftFields.add(new JLabel("Name"));
            leftFields.add(new JLabel(user.name));
            
            JLabel usernameLabel = new JLabel("Username");
            usernameLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            leftFields.add(usernameLabel);
            leftFields.add(new JLabel(user.username));
            
            JLabel roleLabel = new JLabel("Role");
            roleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            leftFields.add(roleLabel);
            leftFields.add(new JLabel(user.role));
            
            addExtraLabels(leftFields, user);

            
            
            item.add(leftFields);
            item.add(rightFields);
            displayPanel.add(item);
        }        
    }
}
