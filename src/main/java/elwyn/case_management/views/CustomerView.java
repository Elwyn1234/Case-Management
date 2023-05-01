package elwyn.case_management.views;

import java.util.Date;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import elwyn.case_management.controllers.RecordController;
import elwyn.case_management.controllers.SubscriptionController;
import elwyn.case_management.controllers.SubscriptionToCustomerController;
import elwyn.case_management.models.Customer;
import elwyn.case_management.models.Gender;
import elwyn.case_management.models.MiscButton;
import elwyn.case_management.models.Role;
import elwyn.case_management.models.SubscribeActionListener;
import elwyn.case_management.models.Subscription;
import elwyn.case_management.models.SubscriptionToCustomer;
import elwyn.case_management.models.UnsubscribeActionListener;
import net.miginfocom.swing.MigLayout;

public class CustomerView extends RecordView<Customer> {
  JTextComponent firstName;
  JTextComponent secondName;
  JTextComponent sirname;
  JList<String> genderList;
  JTextComponent dayOfBirth;
  JTextComponent monthOfBirth;
  JTextComponent yearOfBirth;
  JTextComponent otherNotes;
  JTextComponent email;
  JTextComponent phoneNumber;
  JTextComponent address;
  JTextComponent city;
  JTextComponent postcode;
  JTextComponent country;

  JLabel firstNameValidityMessage = new JLabel();
  JLabel secondNameValidityMessage = new JLabel();
  JLabel sirnameValidityMessage = new JLabel();
  JLabel dayOfBirthValidityMessage = new JLabel();
  JLabel monthOfBirthValidityMessage = new JLabel();
  JLabel yearOfBirthValidityMessage = new JLabel();
  JLabel dateOfBirthValidityMessage = new JLabel();
  JLabel otherNotesValidityMessage = new JLabel();
  JLabel emailValidityMessage = new JLabel();
  JLabel phoneNumberValidityMessage = new JLabel();
  JLabel addressValidityMessage = new JLabel();
  JLabel cityValidityMessage = new JLabel();
  JLabel postcodeValidityMessage = new JLabel();
  JLabel countryValidityMessage = new JLabel();
  
  protected String pageTitle() { return "Customers"; }
  protected String tabNameOfViewRecords() { return "View Customers"; }
  protected String tabNameOfCreateRecord() { return "Create Customer"; }
  protected String tabNameOfEditRecord() { return "Edit Customer"; }

  public CustomerView(RecordController<Customer> controller) {
    super(controller);
    miscButtonParams = new MiscButton<Customer>(this::subscriptionManagementPanel, CustomerView::shouldShowButton, "Subscriptions", new Dimension(140, 30));

    firstNameValidityMessage.setForeground(Color.RED);
    secondNameValidityMessage.setForeground(Color.RED);
    sirnameValidityMessage.setForeground(Color.RED);
    dayOfBirthValidityMessage.setForeground(Color.RED);
    monthOfBirthValidityMessage.setForeground(Color.RED);
    yearOfBirthValidityMessage.setForeground(Color.RED);
    dateOfBirthValidityMessage.setForeground(Color.RED);
    otherNotesValidityMessage.setForeground(Color.RED);
    emailValidityMessage.setForeground(Color.RED);
    phoneNumberValidityMessage.setForeground(Color.RED);
    addressValidityMessage.setForeground(Color.RED);
    cityValidityMessage.setForeground(Color.RED);
    postcodeValidityMessage.setForeground(Color.RED);
    countryValidityMessage.setForeground(Color.RED);
  }

  protected void addRecordManagementFields(JComponent leftPanel, JComponent rightPanel, Customer record) {
    firstNameValidityMessage.setVisible(false);
    secondNameValidityMessage.setVisible(false);
    sirnameValidityMessage.setVisible(false);
    dayOfBirthValidityMessage.setVisible(false);
    monthOfBirthValidityMessage.setVisible(false);
    yearOfBirthValidityMessage.setVisible(false);
    dateOfBirthValidityMessage.setVisible(false);
    otherNotesValidityMessage.setVisible(false);
    emailValidityMessage.setVisible(false);
    phoneNumberValidityMessage.setVisible(false);
    addressValidityMessage.setVisible(false);
    cityValidityMessage.setVisible(false);
    postcodeValidityMessage.setVisible(false);
    countryValidityMessage.setVisible(false);

    firstName = addTextField(leftPanel, "First Name", record.firstName, false, true);
    leftPanel.add(firstNameValidityMessage);
    secondName = addTextField(leftPanel, "Second Name", record.secondName, true, true);
    leftPanel.add(secondNameValidityMessage);
    sirname = addTextField(leftPanel, "Sirname", record.sirname, true, true);
    leftPanel.add(sirnameValidityMessage);
    String dayOfBirthString = "";
    String monthOfBirthString = "";
    String yearOfBirthString = "";
    if (record.dateOfBirth != null) {
      dayOfBirthString = Integer.toString(record.dateOfBirth.getDay());
      monthOfBirthString = Integer.toString(record.dateOfBirth.getMonth() + 1);
      yearOfBirthString = Integer.toString(record.dateOfBirth.getYear() + 1900);
    }
    dayOfBirth = addTextField(leftPanel, "Day of Birth", dayOfBirthString, true, true);
    leftPanel.add(dayOfBirthValidityMessage);
    monthOfBirth = addTextField(leftPanel, "Month of Birth", monthOfBirthString, true, true);
    leftPanel.add(monthOfBirthValidityMessage);
    yearOfBirth = addTextField(leftPanel, "Year of Birth", yearOfBirthString, true, true);
    leftPanel.add(yearOfBirthValidityMessage);
    leftPanel.add(dateOfBirthValidityMessage);
    otherNotes = addTextField(leftPanel, "OtherNotes", record.otherNotes, true, true);
    leftPanel.add(otherNotesValidityMessage);
    email = addTextField(leftPanel, "Email", record.email, true, true);
    leftPanel.add(emailValidityMessage);
    phoneNumber = addTextField(leftPanel, "PhoneNumber", record.phoneNumber, true, true);
    leftPanel.add(phoneNumberValidityMessage);
    address = addTextField(leftPanel, "Address", record.address, true, true);
    leftPanel.add(addressValidityMessage);
    city = addTextField(leftPanel, "City", record.city, true, true);
    leftPanel.add(cityValidityMessage);
    postcode = addTextField(leftPanel, "Postcode", record.postcode, true, true);
    leftPanel.add(postcodeValidityMessage);
    country = addTextField(leftPanel, "Country", record.country, true, true);
    leftPanel.add(countryValidityMessage);
    genderList = addSelectList(leftPanel, "Gender", Gender.stringValues(), null);
  }

  protected void addRecordFields(JComponent leftPanel, JComponent rightPanel, Customer record, boolean editable) {
    if (record == null) {
      record = new Customer();
    }
    if (editable) {
      addRecordManagementFields(leftPanel, rightPanel, record);
      return;
    }

    MigLayout mig = new MigLayout("wrap 2");
    leftPanel.setLayout(mig);
    String font = getFont().getFontName();

    JTextComponent title = RecordView.createTextArea(record.fullNameAndId());
    title.setPreferredSize(new Dimension(300, 50));
    title.setFont(new Font(font, Font.PLAIN, 25));

    JLabel emailLabel = new JLabel();
    if (record.email != null)
      emailLabel = new JLabel(record.email);
    emailLabel.setFont(new Font(font, Font.PLAIN, 14));

    JLabel phoneNumberLabel = new JLabel();
    if (record.phoneNumber != null)
      phoneNumberLabel = new JLabel(record.phoneNumber);
    phoneNumberLabel.setFont(new Font(font, Font.PLAIN, 14));

    Box dateOfBirthBox = new Box(BoxLayout.X_AXIS);
    if (record.dateOfBirth != null)
      dateOfBirthBox = RecordView.createLabelledFieldInline("Date of Birth", record.dateOfBirth.toString(), font);

    Box genderBox = new Box(BoxLayout.X_AXIS);
    if (record.gender != null)
      genderBox = RecordView.createLabelledFieldInline("Gender", record.gender.toString(), font);
    
    Box locationBox = new Box(BoxLayout.X_AXIS);
    if (record.fullAddress() != null && !record.fullAddress().equals(""))
      locationBox = RecordView.createLabelledFieldInline("Location", record.fullAddress(), font);

    Box otherNotesBox = RecordView.createLabelledTextArea("Notes", record.otherNotes, new Insets(TOP_MARGIN, 0, 0, 0));
    
    JLabel activeSubscriptionsLabel = new JLabel("Active Subscriptions");
    activeSubscriptionsLabel.setFont(new Font(font, Font.BOLD, 15));
    activeSubscriptionsLabel.setBorder(new EmptyBorder(new Insets(TOP_MARGIN, 0, 0, 0)));

    SubscriptionToCustomerController subToCustController = new SubscriptionToCustomerController(controller.loggedInUser, record.id);
    SubscriptionController subscriptionController = new SubscriptionController(controller.loggedInUser);

    leftPanel.add(title, "span, aligny top");
    leftPanel.add(emailLabel, "span, aligny top");
    leftPanel.add(phoneNumberLabel, "span, aligny top");
    leftPanel.add(dateOfBirthBox, "span, aligny top");
    leftPanel.add(genderBox, "span, aligny top");
    leftPanel.add(locationBox, "span, aligny top");
    leftPanel.add(otherNotesBox, "span, aligny top");

    leftPanel.add(activeSubscriptionsLabel, "span, aligny top");
    for (SubscriptionToCustomer subToCust : subToCustController.readRecords(0)) {
      Subscription subscription = subscriptionController.readRecord(subToCust.subscription);
      leftPanel.add(createSubscriptionSummaryBox(subToCust, subscription), "span, aligny top");
    }
  }

  public JComponent subscriptionManagementPanel(Long rowid) {
    MigLayout mig = new MigLayout("wrap 1");
    Dimension expectedDimension = new Dimension(500, 630);
    JPanel panel = new JPanel();
    panel.setLayout(mig);
    panel.setPreferredSize(expectedDimension);
    panel.setMaximumSize(expectedDimension);
    panel.setMinimumSize(expectedDimension);

    JButton subscribeButton = new JButton("Subscribe");
    subscribeButton.addActionListener(new SubscribeActionListener(panel, rowid, this));
    panel.add(subscribeButton);

    if (controller.loggedInUser.role != Role.AGENT) {
      JButton unsubscribeButton = new JButton("Unsubscribe");
      unsubscribeButton.addActionListener(new UnsubscribeActionListener(panel, rowid, this));
      panel.add(unsubscribeButton);
    }

    return panel;
  }

  public static Boolean shouldShowButton(Customer user) {
    return true;
  }
    
  protected Customer validateFormValues() {
    firstNameValidityMessage.setText("");
    firstNameValidityMessage.setVisible(false);
    secondNameValidityMessage.setText("");
    secondNameValidityMessage.setVisible(false);
    sirnameValidityMessage.setText("");
    sirnameValidityMessage.setVisible(false);
    dayOfBirthValidityMessage.setText("");
    dayOfBirthValidityMessage.setVisible(false);
    monthOfBirthValidityMessage.setText("");
    monthOfBirthValidityMessage.setVisible(false);
    yearOfBirthValidityMessage.setText("");
    yearOfBirthValidityMessage.setVisible(false);
    dateOfBirthValidityMessage.setText("");
    dateOfBirthValidityMessage.setVisible(false);
    otherNotesValidityMessage.setText("");
    otherNotesValidityMessage.setVisible(false);
    emailValidityMessage.setText("");
    emailValidityMessage.setVisible(false);
    phoneNumberValidityMessage.setText("");
    phoneNumberValidityMessage.setVisible(false);
    addressValidityMessage.setText("");
    addressValidityMessage.setVisible(false);
    cityValidityMessage.setText("");
    cityValidityMessage.setVisible(false);
    postcodeValidityMessage.setText("");
    postcodeValidityMessage.setVisible(false);
    countryValidityMessage.setText("");
    countryValidityMessage.setVisible(false);

    boolean formIsValid = true;
    Customer record = new Customer();

    // First Name
    record.firstName = firstName.getText();
    if (record.firstName.isBlank()) {
      firstNameValidityMessage.setText("First Name is required");
      firstNameValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.firstName.length() > 32) {
      firstNameValidityMessage.setText("First Name must be no more than 32 characters");
      firstNameValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Second Name
    record.secondName = secondName.getText();
    if (record.secondName.length() > 32) {
      secondNameValidityMessage.setText("Second Name must be no more than 32 characters");
      secondNameValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Sirname
    record.sirname = sirname.getText();
    if (record.sirname.length() > 32) {
      sirname.setText("Sirname must be no more than 32 characters");
      sirname.setVisible(true);
      formIsValid = false;
    }

    // Gender
    if (!genderList.isSelectionEmpty()) {
      record.gender = Gender.parseSelectedGender(genderList.getSelectedValue());
    }

    // Date of Birth
    int day = 0;
    int month = 0;
    int year = 0;
    try {
      if (!dayOfBirth.getText().isBlank())
        day = Integer.parseInt(dayOfBirth.getText());
    } catch (Exception e) {
      dayOfBirthValidityMessage.setText("Day of Birth must be a number");
      dayOfBirthValidityMessage.setVisible(true);
      formIsValid = false;
    }
    try {
      if (!monthOfBirth.getText().isBlank())
        month = Integer.parseInt(monthOfBirth.getText());
        month--;
    } catch (Exception e) {
      monthOfBirthValidityMessage.setText("Month of Birth must be a number");
      monthOfBirthValidityMessage.setVisible(true);
      formIsValid = false;
    }
    try {
      if (!yearOfBirth.getText().isBlank())
        year = Integer.parseInt(yearOfBirth.getText()) - 1900;
    } catch (Exception e) {
      yearOfBirthValidityMessage.setText("Year of Birth must be a number");
      yearOfBirthValidityMessage.setVisible(true);
      formIsValid = false;
    }
    record.dateOfBirth = new Date(year, month, day);
    boolean invalidDateGiven =
        record.dateOfBirth.getDate() != day ||
        record.dateOfBirth.getMonth() != month ||
        record.dateOfBirth.getYear() != year;
    boolean dateWasGiven =
        !yearOfBirth.getText().isBlank() ||
        !monthOfBirth.getText().isBlank() ||
        !dayOfBirth.getText().isBlank();
    if (dateWasGiven && invalidDateGiven) {
      dateOfBirthValidityMessage.setText("The date given is not valid");
      dateOfBirthValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Other Notes
    record.otherNotes = otherNotes.getText();

    // Email
    record.email = email.getText();
    if (record.email.length() > 128) {
      emailValidityMessage.setText("Email must be no more than 128 characters");
      emailValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Phone Number
    record.phoneNumber = phoneNumber.getText();
    if (record.phoneNumber.isBlank()) {
      phoneNumberValidityMessage.setText("Phone number is required");
      phoneNumberValidityMessage.setVisible(true);
      formIsValid = false;
    }
    if (record.phoneNumber.length() > 32) { // eTODO: regex validation
      phoneNumberValidityMessage.setText("Phone number must be no more than 32 characters");
      phoneNumberValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Address
    record.address = address.getText();
    if (record.address.length() > 32) { // eTODO: regex validation
      addressValidityMessage.setText("Address must be no more than 32 characters");
      addressValidityMessage.setVisible(true);
      formIsValid = false;
    }
    
    // City
    record.city = city.getText();
    if (record.city.length() > 32) { // eTODO: regex validation
      cityValidityMessage.setText("City must be no more than 32 characters");
      cityValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Postcode
    record.postcode = postcode.getText();
    if (record.city.length() > 32) { // eTODO: regex validation
      postcodeValidityMessage.setText("Postcode must be no more than 32 characters");
      postcodeValidityMessage.setVisible(true);
      formIsValid = false;
    }

    // Country
    record.country = country.getText();
    if (record.country.length() > 32) { // eTODO: regex validation
      countryValidityMessage.setText("Country must be no more than 32 characters");
      countryValidityMessage.setVisible(true);
      formIsValid = false;
    }

    if (formIsValid)
      return record;
    else
      return null;
  }
  
  public static JPanel createCustomerSummaryBox(Customer customer, String fontName, boolean leftAlign) {
    JPanel panel = new JPanel();
    panel.setLayout(new MigLayout("wrap 1"));
    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    if (customer == null) 
      return panel;

    String openedForText = customer.fullNameAndId();

    JLabel customerSubsectionLabel = new JLabel("Customer (" + customer.id + ")");
    JLabel customerNameLabel = new JLabel(openedForText, SwingConstants.RIGHT);
    JLabel customerEmailLabel = new JLabel(customer.email);
    JLabel customerPhoneNumberLabel = new JLabel(customer.phoneNumber);

    customerNameLabel.setFont(new Font(fontName, Font.PLAIN, 14));
    customerEmailLabel.setFont(new Font(fontName, Font.PLAIN, 14));
    customerPhoneNumberLabel.setFont(new Font(fontName, Font.PLAIN, 14));

    String s = "align right";
    if (leftAlign)
      s = "align left";
    panel.add(customerSubsectionLabel, s);
    panel.add(customerNameLabel, s);
    panel.add(customerEmailLabel, s);
    panel.add(customerPhoneNumberLabel, s);
    return panel;
  }

  public JComponent createSubscriptionSummaryBox(SubscriptionToCustomer subToCust, Subscription subscription) {
    Box box = new Box(BoxLayout.Y_AXIS);

    String font = getFont().getFontName();

    JLabel subscriptionName = new JLabel();
    if (subscription.name != null && !subscription.name.isBlank())
      subscriptionName = new JLabel(subscription.name);
    subscriptionName.setFont(new Font(font, Font.BOLD, 14));

    JLabel price = new JLabel();
    if (subscription.frequency != null)
      price = new JLabel(subscription.pricePerPeriod());
    price.setFont(new Font(font, Font.PLAIN, 14));

    JLabel sinceWhen = new JLabel();
    if (subToCust.dateStarted != null)
      sinceWhen = new JLabel(subToCust.dateStarted.toString());
    sinceWhen.setFont(new Font(font, Font.PLAIN, 14));

    box.add(subscriptionName);
    box.add(price);
    box.add(sinceWhen);
    return box;
  }
}
