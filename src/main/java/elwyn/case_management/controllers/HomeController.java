package elwyn.case_management.controllers;

import java.util.ArrayList;
import java.util.List;

import elwyn.case_management.models.Case;
import elwyn.case_management.models.Contact;
import elwyn.case_management.models.User;

public class HomeController {
  public User user; // eTODO: getter setter

  public HomeController(User user) {
    this.user = user;
  }

  public List<Contact> selectMyContacts(List<Contact> contacts) {
    List<Contact> filteredContacts = new ArrayList<Contact>();
    for (Contact contact : contacts) {
      if (contact.user.id == user.id)
        filteredContacts.add(contact);
    }
    return filteredContacts;
  }

  public List<Case> selectMyCases(List<Case> cases) {
    List<Case> filteredCases = new ArrayList<Case>();
    for (Case caseRecord : cases) {
      if (caseRecord.assignedTo.id == user.id)
        filteredCases.add(caseRecord);
    }
    return filteredCases;
  }
}
