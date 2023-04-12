package elwyn.case_management.controllers;

import java.util.ArrayList;
import java.util.List;

import elwyn.case_management.models.Case;
import elwyn.case_management.models.Contact;

public class HomeController {
  public static List<Contact> selectMyContacts(List<Contact> contacts) {
    List<Contact> filteredContacts = new ArrayList<Contact>();
    for (Contact contact : contacts) {
      if (contact.user.id == 1)
        filteredContacts.add(contact);
    }
    return filteredContacts;
  }
  public static List<Case> selectMyCases(List<Case> cases) {
    List<Case> filteredCases = new ArrayList<Case>();
    for (Case caseRecord : cases) {
      if (caseRecord.user.id == 1)
        filteredCases.add(caseRecord);
    }
    return filteredCases;
  }
}
