package elwyn.clinic.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.text.JTextComponent;

import elwyn.clinic.controllers.RecordController;
import elwyn.clinic.controllers.UserController;
import elwyn.clinic.models.Role;
import elwyn.clinic.models.User;

public class UserView extends RecordView<User> {
  JTextComponent name; JTextComponent username;
  JTextComponent password;
  JList<String> roles;
  // Contacts List

  JLabel nameValidityMessage = new JLabel();
  JLabel usernameValidityMessage = new JLabel();
  JLabel passwordValidityMessage = new JLabel();
  JLabel rolesValidityMessage = new JLabel();
  Boolean editing = false;

  protected String pageTitle() { return "Users"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Users"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create User"; }
  protected String tabNameOfEditRecord() { return "Edit User"; }
  @Override
  protected String migLayoutString() { return "wrap 5, alignx center"; };


  public UserView(RecordController<User> controller) {
    super(controller);
    nameValidityMessage.setForeground(Color.RED);
    usernameValidityMessage.setForeground(Color.RED);
    passwordValidityMessage.setForeground(Color.RED);
    rolesValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, User record) {
    nameValidityMessage.setVisible(false);
    usernameValidityMessage.setVisible(false);
    passwordValidityMessage.setVisible(false);
    rolesValidityMessage.setVisible(false);

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
    String font = getFont().getFontName();
    Dimension expectedDimension = new Dimension(300, 200);
    leftPanel.setMinimumSize(expectedDimension);

    JTextComponent title = RecordView.createTextArea(record.name + " (" + record.id + ")");
    title.setPreferredSize(new Dimension(300, 50));
    title.setFont(new Font(font, Font.PLAIN, 25));

    Box usernameBox = new Box(BoxLayout.X_AXIS);
    if (record.username != null)
      usernameBox = RecordView.createLabelledFieldInline("Username", record.username, font);

    Box roleBox = new Box(BoxLayout.X_AXIS);
    if (record.role != null)
      roleBox = RecordView.createLabelledFieldInline("Role", record.role.toString(), font);

    leftPanel.add(title);
    leftPanel.add(usernameBox);
    leftPanel.add(roleBox);
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
    if (existingUser != null && !editing && record.username.equals(existingUser.username)) { // eTODO: fix
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

    if (formIsValid)
      return record;
    else
      return null;
  }
}

