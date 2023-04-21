package elwyn.case_management.views;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.text.JTextComponent;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.models.Role;
import elwyn.case_management.models.User;

public class UserView extends RecordView<User> {
  JTextComponent name;
  JTextComponent username;
  JTextComponent password;
  JList<String> roles;
  // Contacts List

  JLabel nameValidityMessage = new JLabel();
  JLabel usernameValidityMessage = new JLabel();
  JLabel passwordValidityMessage = new JLabel();
  JLabel rolesValidityMessage = new JLabel();

  protected String pageTitle() { return "Users"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Users"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create User"; }
  protected String tabNameOfEditRecord() { return "Edit User"; }

  public UserView(RecordController<User> controller) {
    super(controller, null);

    nameValidityMessage.setVisible(false);
    usernameValidityMessage.setVisible(false);
    passwordValidityMessage.setVisible(false);
    rolesValidityMessage.setVisible(false);

    nameValidityMessage.setForeground(Color.RED);
    usernameValidityMessage.setForeground(Color.RED);
    passwordValidityMessage.setForeground(Color.RED);
    rolesValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, User record) {
    name = addTextField(leftPanel, "Name", record.name, false, true);
    leftPanel.add(nameValidityMessage);
    username = addTextField(leftPanel, "Username", record.username, false, true);
    leftPanel.add(usernameValidityMessage);
    password = addTextField(leftPanel, "Password", record.password, false, true);
    leftPanel.add(passwordValidityMessage);

    String role = record.role == null ? null : record.role.toString();
    roles = addSelectList(leftPanel, "Role", Role.stringValues(), role);
    leftPanel.add(rolesValidityMessage);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, User record, boolean editable) {
    if (record == null) {
      record = new User();
    }
    if (editable) {
      addRecordManagementFields(leftPanel, rightPanel, record);
      return;
    }

    //eTODO: rename from "" to "Field"
    name = addTextField(leftPanel, "Name", record.name, false, editable);
    username = addTextField(leftPanel, "Username", record.username, false, editable);
    password = addTextField(leftPanel, "Password", record.password, false, editable);

    String role = record.role == null ? null : record.role.toString();
    addTextField(leftPanel, "Role", role, true, false);
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

    // Password
    record.password = password.getText();
    if (record.password.isBlank()) {
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

    if (formIsValid)
      return record;
    else
      return null;
  }
}

