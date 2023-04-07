package elwyn.case_management.views;

import java.awt.Color;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import elwyn.case_management.models.User;

public class UserView {
  final static String viewUsersTabName = "View Users";
  final static String createUserTabName = "Create User";
  final static String editUserTabName = "Edit User";
  List<User> users;
  JPanel displayPanel;
  JTabbedPane tabbedPane;
  JPanel viewUsersPanel;
  Connection conn;
  JTextField nameTextField;
  JTextField usernameTextField;
  JTextField passwordTextField;
  JList<String> roleList;
    
  public UserView(JPanel displayPanel) {
    try {
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:assets/caseManagement.db");
    } catch (Exception ex) {
      // eTODO: logging
      // Logger.getLogger(UserManagementView.class.getName()).log(Level.SEVERE, null, ex);
    }
    this.displayPanel = displayPanel;
    initComponents();
  }
    
  public void loadUsers() {
    users = new ArrayList<User>();
    try {
      String sql = "SELECT rowid, * from users";
      PreparedStatement pStatement = conn.prepareStatement(sql);
      ResultSet rs = pStatement.executeQuery();
            
      while (rs.next()) {
        User user = new User();
        user.id = rs.getLong("rowid");
        user.name = rs.getString("name");
        user.username = rs.getString("username");
        user.password = rs.getString("password");
        user.role = parseSelectedRole(rs.getString("role"));
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
    // VIEW USERS COMPONENTS
    viewUsersPanel = new JPanel();
    viewUsersPanel.setSize(1028, 330);
    viewUsersPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    viewUsersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
    viewUsersPanel.addComponentListener(new ComponentListener() {
        @Override
        public void componentShown(ComponentEvent e) {
          createUsersListPanel();
        }
        @Override
        public void componentResized(ComponentEvent e) {}
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
    });



    // TABBED PANE SETUP
    JPanel createUserPanel = new JPanel();
    createUserPanel.setSize(1028, 330);
    createUserPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    createUserPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
    createUserPanel.add(createUserManagementPanel(null, true));

    tabbedPane = new JTabbedPane();
    tabbedPane.addTab(viewUsersTabName, viewUsersPanel);
    tabbedPane.addTab(createUserTabName, createUserPanel);
    
    displayPanel.add(tabbedPane);
  }

  private boolean validateUserCreationFields(User user) {
    if (user.name.length() <= 0) {
      return false;
    }
    if (user.username.length() <= 0) {
      return false;
    }
    if (user.password.length() <= 0) {
      return false; // eTODO: password validation
    }
    if (user.role == null) {
      return false;
    }
    return true;
  }
  private User.Role parseSelectedRole(String rawRole) {
    if (rawRole == null)
      return null;
    if (rawRole.equals("AGENT")) {
      return User.Role.AGENT;
    }
    if (rawRole.equals("LEADER")) {
      return User.Role.LEADER;
    }
    if (rawRole.equals("ADMIN")) {
      return User.Role.ADMIN;
    }
    return null;
  }
  
  private void createUsersListPanel() {
    loadUsers();
    viewUsersPanel.removeAll();
    for (var user : users) {
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
      leftFields.add(new JLabel(user.role.toString()));
      
      addExtraLabels(leftFields, user);

      JButton editButton = new JButton("Edit");
      editButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent event) {
            tabbedPane.addTab(editUserTabName, createUserManagementPanel(user, false));;
            tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(editUserTabName));
          }
      });
      viewUsersPanel.add(editButton);

      JButton deleteButton = new JButton("Delete");
      deleteButton.addActionListener(new ActionListener() {
          String username = user.username;
          @Override
          public void actionPerformed(ActionEvent event) {
            try {
              String sql = "DELETE FROM users where username=?;";
              PreparedStatement pStatement = conn.prepareStatement(sql);
              pStatement.setString(1, username);
              pStatement.executeUpdate(); // eTODO: Do we need to handle the return value
            } catch (Exception e) {
              System.out.println("Error: " + e.getMessage());
              e.printStackTrace();
            }
            createUsersListPanel();
          }
      });
      viewUsersPanel.add(deleteButton);
      viewUsersPanel.add(leftFields);
      viewUsersPanel.add(rightFields);
    }        
    viewUsersPanel.revalidate();
  }

  private JPanel createUserManagementPanel(User user, boolean createMode) {
    User tempUser = user; // store the pointer to User so that it can be restored at the end of the function.
    if (createMode) {
      user = new User();
    }
    JPanel manageUserPanel = new JPanel();
    manageUserPanel.setSize(300, 330);
    manageUserPanel.setLayout(new BoxLayout(manageUserPanel, BoxLayout.Y_AXIS));
              
    manageUserPanel.add(new JLabel("Name"));
    nameTextField = new JTextField(user.name);
    manageUserPanel.add(nameTextField);
              
    JLabel usernameLabel = new JLabel("Username");
    usernameLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
    manageUserPanel.add(usernameLabel);
    usernameTextField = new JTextField(user.username);
    manageUserPanel.add(usernameTextField);
            
    JLabel passwordLabel = new JLabel("Password");
    passwordLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
    manageUserPanel.add(passwordLabel);
    passwordTextField = new JTextField(user.password); // eTODO: HASHING
    manageUserPanel.add(passwordTextField); // eTODO: confirm password field
            
    JLabel roleLabel = new JLabel("Role");
    roleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
    manageUserPanel.add(roleLabel);
    roleList = new JList<String>(new String[]{"AGENT", "LEADER", "ADMIN"});
    if (!createMode) {
      roleList.setSelectedValue(user.role.toString(), false);
    }
    manageUserPanel.add(roleList);

    long userId = user.id;
    String buttonText = createMode ? "Create" : "Update";
    JButton commitButton = new JButton(buttonText);
    commitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          try {
            User user = new User();
            user.id = userId;
            user.name = nameTextField.getText();
            user.username = usernameTextField.getText();
            user.password = passwordTextField.getText();
            user.role = parseSelectedRole(roleList.getSelectedValue());
            if (!validateUserCreationFields(user)) {
              // eTODO: Display to user
              return;
            }
            String sql;
            if (createMode) {
              sql = "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?);";
            } else {
              sql = "UPDATE users SET name=?, username=?, password=?, role=? where rowid=?;";
            }
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, user.name);
            pStatement.setString(2, user.username);
            pStatement.setString(3, user.password); // eTODO: hashing
            pStatement.setString(4, user.role.toString());
            if (!createMode) {
              pStatement.setLong(5, user.id);
            }
            pStatement.executeUpdate();
          } catch (Exception e) {
              System.out.println("Error: " + e.getMessage());
              e.printStackTrace();
          }
          if (!createMode) {
            int indexOfTab = tabbedPane.indexOfTab(editUserTabName);
            if (indexOfTab != -1) {
              tabbedPane.removeTabAt(indexOfTab);;
              tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(viewUsersTabName));
            }
          }
          // eTODO: user feedback
      }
    });
    manageUserPanel.add(commitButton);
    user = tempUser;
    return manageUserPanel;
  }
}

