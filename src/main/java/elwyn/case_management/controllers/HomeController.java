package elwyn.case_management.controllers;

import elwyn.case_management.models.User;

public class HomeController {
  public User user; // eTODO: getter setter

  public HomeController(User user) {
    this.user = user;
  }
}
