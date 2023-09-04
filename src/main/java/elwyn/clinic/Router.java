package elwyn.clinic;

import javax.swing.JFrame;
import javax.swing.JComponent;

import elwyn.clinic.controllers.HomeController;
import elwyn.clinic.controllers.UserController;
import elwyn.clinic.models.RouterModel;
import elwyn.clinic.models.User;
import elwyn.clinic.models.View;
import elwyn.clinic.views.HomeView;
import elwyn.clinic.views.LoginView;

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
        displayedComponent = new LoginView(new UserController(null), this::displayView);
        break;
    }
    frame.add(displayedComponent);
    frame.revalidate();
  }
}
