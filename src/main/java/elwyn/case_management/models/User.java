package elwyn.case_management.models;

import java.util.ArrayList;
import java.util.List;

public class User extends Record {
  public String name;
  public String username;
  public String password; // eTODO: hashed?
  public Role role;
  public Long teamLead;

  public String fullNameAndId() { 
    return name + " (" + id + ")";
  }

  public List<Contact> selectMyContacts(List<Contact> contacts) {
    List<Contact> filteredContacts = new ArrayList<Contact>();
    for (Contact contact : contacts) {
      if (contact == null || contact.user == null)
        continue;
      if (contact.user.id == id)
        filteredContacts.add(contact);
    }
    return filteredContacts;
  }

  public List<Case> selectMyCases(List<Case> cases) {
    List<Case> filteredCases = new ArrayList<Case>();
    for (Case caseRecord : cases) {
      if (caseRecord == null || caseRecord.assignedTo == null)
        continue;
      if (caseRecord.assignedTo.id == id)
        filteredCases.add(caseRecord);
    }
    return filteredCases;
  }

  @Override
  public String toString(int depth) {
    String oneIndent = "        ";
    String indent1 = "";
    String indent2 = oneIndent;
    for (int i = 0; i < depth; i++) {
      indent1 += oneIndent;
      indent2 += oneIndent;
    }
    String val = "{\n";
    if (id != 0)
      val += indent2 + "id: " + id + "\n";
    val += indent2 + "name: " + name + "\n";
    val += indent2 + "username: " + username + "\n";
    val += indent2 + "password: " + "Value hidden" + "\n";
    val += indent2 + "role: " + role + "\n";
    val += indent2 + "teamLead: " + teamLead + "\n";
    val += indent1 + "}";
    return val;
  }
}
