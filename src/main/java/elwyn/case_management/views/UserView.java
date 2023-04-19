package elwyn.case_management.views;

import javax.swing.JComponent;
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

  protected String pageTitle() { return "Users"; } // eTODO: use these again to add titles
  protected String tabNameOfViewRecords() { return "View Users"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create User"; }
  protected String tabNameOfEditRecord() { return "Edit User"; }

  public UserView(RecordController<User> controller) {
    super(controller, null);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, User record, boolean editable) {
    if (record == null) {
      record = new User();
    }
    name = addTextField(leftPanel, "Name", record.name, false, editable); //eTODO: rename from "" to "Field"
    username = addTextField(leftPanel, "Username", record.username, false, editable); //eTODO: rename from "" to "Field"
    password = addTextField(leftPanel, "Password", record.password, false, editable); //eTODO: rename from "" to "Field"

    String role = record.role == null ? null : record.role.toString();
    if (editable)
      roles = addSelectList(leftPanel, "Role", Role.stringValues(), role);
    else
      addTextField(leftPanel, "Role", role, true, false);
  }
    
  protected User validateFormValues() {
    User record = new User();
    record.name = name.getText();
    record.username = username.getText();
    record.password = password.getText();
    record.role = Role.parseSelectedRole(roles.getSelectedValue()); //eTODO: rename parseSelectedX mthods
    return record;
  }
}

