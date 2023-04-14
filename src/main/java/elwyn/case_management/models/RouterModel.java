package elwyn.case_management.models;

public class RouterModel {
  public View view;
  public User user;

  public RouterModel(View view, User user) {
    this.view = view;
    this.user = user;
  }
}
