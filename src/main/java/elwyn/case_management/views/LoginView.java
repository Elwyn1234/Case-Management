package elwyn.case_management.views;

import java.awt.event.*;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;
import javax.swing.JLabel;

import elwyn.case_management.models.RouterModel;
import elwyn.case_management.models.User;
import elwyn.case_management.models.View;
import elwyn.case_management.controllers.UserController;

public class LoginView extends JScrollPane {
  UserController userController;
  Consumer<RouterModel> displayView;
  JTextComponent usernameField;
  JTextComponent passwordField;

  public LoginView(UserController userController, Consumer<RouterModel> displayView) {
    this.userController = userController;
    this.displayView = displayView;
    displayLogin();
  }

  public void displayLogin() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Login"));
    usernameField = RecordView.addTextField(panel, "Username", "", false, true);
    passwordField = RecordView.addTextField(panel, "Password", "", false, true);

    JButton loginButton = new JButton("Login");
    loginButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          String username = usernameField.getText();
          String password = passwordField.getText();
          if (userController.areCredentialsValid(username, password)) {
            User user = userController.readRecord(username);
            displayView.accept(new RouterModel(View.HOME, user)); // eTODO: shall we add a Router class that handles which view has ownership
          }
        }
    });
    panel.add(loginButton);
    setViewportView(panel);
  }
}

