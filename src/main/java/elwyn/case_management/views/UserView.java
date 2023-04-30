package elwyn.case_management.views;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.controllers.UserController;
import elwyn.case_management.models.Role;
import elwyn.case_management.models.User;

public class UserView extends RecordView<User> {
  JTextComponent name; JTextComponent username;
  JTextComponent password;
  JList<String> roles;
  JTextComponent teamLead;
  // Contacts List

  JLabel nameValidityMessage = new JLabel();
  JLabel usernameValidityMessage = new JLabel();
  JLabel passwordValidityMessage = new JLabel();
  JLabel rolesValidityMessage = new JLabel();
  JLabel teamLeadValidityMessage = new JLabel();
  Boolean editing = false;

  protected String pageTitle() { return "Users"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Users"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create User"; }
  protected String tabNameOfEditRecord() { return "Edit User"; }

  public UserView(RecordController<User> controller) {
    super(controller);
    nameValidityMessage.setForeground(Color.RED);
    usernameValidityMessage.setForeground(Color.RED);
    passwordValidityMessage.setForeground(Color.RED);
    rolesValidityMessage.setForeground(Color.RED);
    teamLeadValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, User record) {
    nameValidityMessage.setVisible(false);
    usernameValidityMessage.setVisible(false);
    passwordValidityMessage.setVisible(false);
    rolesValidityMessage.setVisible(false);
    teamLeadValidityMessage.setVisible(false);

    name = addTextField(leftPanel, "Name", record.name, false, true);
    leftPanel.add(nameValidityMessage);

    username = addTextField(leftPanel, "Username", record.username, false, true);
    leftPanel.add(usernameValidityMessage);

    String passwordLabel = "Password";
    if (editing)
      passwordLabel = "Password - leave empty to keep old password";
    password = addTextField(leftPanel, passwordLabel, "", false, true);
    leftPanel.add(passwordValidityMessage);

    String role = record.role == null ? null : record.role.toString();
    roles = addSelectList(leftPanel, "Role", Role.stringValues(), role);
    leftPanel.add(rolesValidityMessage);

    String teamLeadString = record.teamLead == null ? "" : Long.toString(record.teamLead);
    teamLead = addTextField(leftPanel, "Team Lead", teamLeadString, false, true);
    leftPanel.add(teamLeadValidityMessage);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, User record, boolean editable) {
    if (record == null) {
      editing = false;
      record = new User();
    } else {
      editing = true;
    }
    if (editable) {
      addRecordManagementFields(leftPanel, rightPanel, record);
      return;
    }
    editing = false;

    //eTODO: rename from "" to "Field"
    addTextField(leftPanel, "Name", record.name, false, editable);

    addTextField(leftPanel, "Username", record.username, false, editable);

    String role = record.role == null ? null : record.role.toString();
    addTextField(leftPanel, "Role", role, true, false);

    String teamLeadString = record.teamLead == null ? "" : Long.toString(record.teamLead);
    addTextField(leftPanel, "Team Lead", teamLeadString, false, editable);
  }

  protected User validateFormValues() {
    nameValidityMessage.setText("");
    nameValidityMessage.setVisible(false);
    usernameValidityMessage.setText("");
    usernameValidityMessage.setVisible(false);
    passwordValidityMessage.setText("");
    passwordValidityMessage.setVisible(false);
    rolesValidityMessage.setText("");
    rolesValidityMessage.setVisible(false);
    teamLeadValidityMessage.setText("");
    teamLeadValidityMessage.setVisible(false);

    UserController userController = new UserController(null);
    boolean formIsValid = true;
    User record = new User();

    // Name
    record.name = name.getText();
    if (record.name.isBlank()) {
      nameValidityMessage.setText("Name is required");
      nameValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.name.length() > 32) {
      nameValidityMessage.setText("Name must be no more than 32 characters");
      nameValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Username
    record.username = username.getText();
    if (record.username.isBlank()) {
      usernameValidityMessage.setText("Username is required");
      usernameValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.username.length() > 32) {
      usernameValidityMessage.setText("Username must be no more than 32 characters");
      usernameValidityMessage.setVisible(true);
      formIsValid = false;
    }
    User existingUser = userController.readRecord(record.username);
    if (!editing && record.username.equals(existingUser.username)) { // eTODO: fix
      usernameValidityMessage.setText("Username is taken");
      usernameValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Password
    record.password = password.getText();
    if (record.password.isBlank() && !editing) {
      passwordValidityMessage.setText("Password is required");
      passwordValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.password.length() > 32) {
      passwordValidityMessage.setText("Password must be no more than 32 characters");
      passwordValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Role
    if (roles.isSelectionEmpty()) {
      rolesValidityMessage.setText("Role is required");
      rolesValidityMessage.setVisible(true);
      formIsValid = false;
    }
    record.role = Role.parseSelectedRole(roles.getSelectedValue());

    // Team Lead
    if (!teamLead.getText().isBlank()) {
      try {
        record.teamLead = Long.parseLong(teamLead.getText()); // eTODO: handle exception
      } catch (Exception e) {
        teamLeadValidityMessage.setText("Team Lead must be a valid User ID");
        teamLeadValidityMessage.setVisible(true);
        formIsValid = false;
      }
      User user = userController.readRecord(record.teamLead);
      if (user == null) {
        teamLeadValidityMessage.setText("Team Lead must be a valid User ID");
        teamLeadValidityMessage.setVisible(true);
        formIsValid = false;
      }
    }

    if (formIsValid)
      return record;
    else
      return null;
  }
}

