package elwyn.clinic.views;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.event.*;
import java.awt.*;
import java.util.function.Consumer;

import elwyn.clinic.controllers.CustomerController;
import elwyn.clinic.controllers.DiseaseController;
import elwyn.clinic.controllers.HomeController;
import elwyn.clinic.controllers.LogController;
import elwyn.clinic.controllers.MedicineController;
import elwyn.clinic.controllers.UserController;
import elwyn.clinic.models.Role;
import elwyn.clinic.models.RouterModel;
import elwyn.clinic.models.User;
import elwyn.clinic.models.View;
import net.miginfocom.swing.MigLayout;
import elwyn.clinic.controllers.AppointmentController;

public class HomeView extends JTabbedPane {
  JFrame frame;
  HomeController homeController;
  Consumer<RouterModel> displayView;

  public HomeView(HomeController homeController, Consumer<RouterModel> displayView) {
    this.homeController = homeController;
    this.displayView = displayView;
    displayHome();
  }

  public void displayHome() {
    addTab("Home", createHomeView());

    CustomerView customerView = new CustomerView(new CustomerController(homeController.user));
    addTab("Customers", customerView);

    AppointmentView tabAppointmentView = new AppointmentView(new AppointmentController(homeController.user, null));
    addTab("Appointments", tabAppointmentView);

    DiseaseView tabDiseaseView = new DiseaseView(new DiseaseController(homeController.user, null));
    addTab("Diseases", tabDiseaseView);

    MedicineView tabMedicineView = new MedicineView(new MedicineController(homeController.user));
    addTab("Medicines", tabMedicineView);

    if (homeController.user.role == Role.ADMIN) {
      UserView userView = new UserView(new UserController(homeController.user));
      addTab("Users", userView);

      LogView logView = new LogView(new LogController(homeController.user));
      addTab("Logs", logView);

      QuoteOfTheDayView quoteOfTheDayView = new QuoteOfTheDayView();
      addTab("Quote of the Day", quoteOfTheDayView);
    }
  }

  JComponent createHomeView() {
    AppointmentController appointmentController = new AppointmentController(homeController.user, homeController.user::selectMyAppointments);

    JLabel titleLabel = new JLabel("Hello " + homeController.user.name);
    titleLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, 30));
    titleLabel.setBorder(new EmptyBorder(new Insets(30, 0, 0, 0)));

    JLabel appointmentCountLabel = new JLabel("You have " + appointmentController.readRecords(0).size() + " active appointments assigned to you."); // eTODO: show my records
    appointmentCountLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, 18));
    appointmentCountLabel.setBorder(new EmptyBorder(new Insets(30, 0, 0, 0)));

    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          displayView.accept(new RouterModel(View.LOGIN, new User()));
        }
    });

    AppointmentView appointmentView = new AppointmentView(appointmentController);
    appointmentView.setViewportView(appointmentView.displayRecordListing());
    appointmentView.setBorder(new EmptyBorder(new Insets(30, 0, 0, 0)));

    MigLayout mig = new MigLayout("wrap 1, alignx center");
    JPanel panel = new JPanel();
    panel.setLayout(mig);
    panel.add(titleLabel, "alignx center");
    panel.add(appointmentCountLabel, "alignx center"); // eTODO: fix pagination
    panel.add(Box.createVerticalStrut(10));
    panel.add(logoutButton, "alignx center");
    panel.add(appointmentView, "alignx center");



    JScrollPane home = new JScrollPane();
    home.setViewportView(panel);
    home.addComponentListener(new ComponentListener() { // This must be done to handle databases changes that could happen in other tabs
        @Override
        public void componentShown(ComponentEvent e) {
          appointmentView.setViewportView(appointmentView.displayRecordListing());
        }
        @Override
        public void componentResized(ComponentEvent e) {}
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
    });
    return home;
  }
}

//  eTODO:
//    Add notes to customer contacts
//    Cancel Subscriptions
//    display customer id in the customer listing
//    display a list of contacts under the appointments listing
//    add a vip boolean to customer and add this field to the appointments and contacts views as well
//    add status field to appointment records
//    Pull the User View into the new methodology
//    Change the address fields for customers
//    Validation
//    number fields should have a not set representation
//    Redefine how contacts and appointments connect (not all contacts need a appointment) appointments and contacts should have a User field
//    User should have a field defining their team leader
//    Handle User's names similar to that of customers
