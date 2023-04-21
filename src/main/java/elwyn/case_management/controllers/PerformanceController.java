package elwyn.case_management.controllers;

import elwyn.case_management.models.User;

public class PerformanceController {
  public User user; // eTODO: getter setter
  CaseController caseController;
  ContactController contactController;

  public PerformanceController(User user) {
    this.user = user;
    caseController = new CaseController(user::selectMyCases, null);
    contactController = new ContactController(user::selectMyContacts);
  }
}
