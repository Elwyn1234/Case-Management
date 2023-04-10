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

  protected String tabNameOfViewRecords() { return "View Users"; } // eTODO: use these again to add titles
  protected String tabNameOfCreateRecord() { return "Create User"; }
  protected String tabNameOfEditRecord() { return "Edit User"; }

  public UserView(RecordController<User> controller) {
    super(controller);
  }

  protected void addRecordFields(JComponent panel, User record, boolean editable) {
    if (record == null) {
      record = new User();
    }
    name = addTextField(panel, "Name", record.name, false, editable); //eTODO: rename from "" to "Field"
    username = addTextField(panel, "Username", record.username, false, editable); //eTODO: rename from "" to "Field"
    password = addTextField(panel, "Password", record.password, false, editable); //eTODO: rename from "" to "Field"

    String role = record.role == null ? null : record.role.toString();
    if (editable)
      roles = addSelectList(panel, "Role", Role.stringValues(), role);
    else
      addTextField(panel, "Role", role, true, false);
  }
    
  protected User getFormValues() {
    User record = new User();
    record.name = name.getText();
    record.username = username.getText();
    record.password = password.getText();
    record.role = Role.parseSelectedRole(roles.getSelectedValue()); //eTODO: rename parseSelectedX mthods
    return record;
  }
}

