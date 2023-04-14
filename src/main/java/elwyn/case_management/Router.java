package elwyn.case_management;

import javax.swing.JFrame;
import javax.swing.JComponent;

import elwyn.case_management.controllers.HomeController;
import elwyn.case_management.controllers.UserController;
import elwyn.case_management.models.RouterModel;
import elwyn.case_management.models.User;
import elwyn.case_management.models.View;
import elwyn.case_management.views.HomeView;
import elwyn.case_management.views.LoginView;

public class Router {
  JFrame frame;
  JComponent displayedComponent;

  public Router() {
    frame = new JFrame("Case Management");
    frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    displayView(new RouterModel(View.LOGIN, new User()));
    frame.setVisible(true);
  }

  public void displayView(RouterModel router) {
    if (displayedComponent != null)
      frame.remove(displayedComponent);
    switch(router.view) {
      case HOME:
        displayedComponent = new HomeView(new HomeController(router.user), this::displayView); // eTODO: shall we add a Router class that handles which view has ownership
        break;
      case LOGIN:
        displayedComponent = new LoginView(new UserController(), this::displayView);
        break;
    }
    frame.add(displayedComponent);
    frame.revalidate();
  }
}
