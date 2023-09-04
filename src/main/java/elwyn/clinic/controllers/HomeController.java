package elwyn.clinic.controllers;

import elwyn.clinic.models.User;

public class HomeController {
  public User user; // eTODO: getter setter

  public HomeController(User user) {
    this.user = user;
  }
}
