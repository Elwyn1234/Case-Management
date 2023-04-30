package elwyn.case_management.models;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import elwyn.case_management.controllers.SubscriptionController;
import elwyn.case_management.controllers.SubscriptionToCustomerController;
import elwyn.case_management.views.CustomerView;
import elwyn.case_management.views.RecordView;

public class SubscribeActionListener implements ActionListener {
  CustomerView customerView;
  Long customerId;

  SubscriptionToCustomerController subToCustController;
  SubscriptionController subscriptionController = new SubscriptionController(null);

  JList<String> subscriptionNames;
  List<SubscriptionToCustomer> subToCustomers = new ArrayList<SubscriptionToCustomer>();
  JLabel subscriptionNamesValidityMessage = new JLabel();

  public SubscribeActionListener(JPanel panel, Long customerId, CustomerView customerView) {
    this.customerView = customerView;
    this.customerId = customerId;
    subToCustController = new SubscriptionToCustomerController(null, customerId);

    List<Subscription> subscriptions = subscriptionController.readRecords(0);

    String[] subscriptionNamesArray = new String[subscriptions.size()];
    for (int i = 0; i < subscriptions.size(); i++) {
      subscriptionNamesArray[i] = subscriptions.get(i).name;

      SubscriptionToCustomer subToCust = new SubscriptionToCustomer();
      subToCust.subscription = subscriptions.get(i).id;
      subToCust.customer = customerId;
      subToCust.dateStarted = new Date();
      subToCustomers.add(i, subToCust);
    }
    subscriptionNames = RecordView.addSelectList(panel, "Subscriptions", subscriptionNamesArray, null);
    panel.add(subscriptionNamesValidityMessage);

    subscriptionNamesValidityMessage.setForeground(Color.RED);
    subscriptionNamesValidityMessage.setVisible(false);
    subscriptionNamesValidityMessage.setText("");
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    subscriptionNamesValidityMessage.setVisible(false);
    subscriptionNamesValidityMessage.setText("");
    if (subscriptionNames.isSelectionEmpty()) {
      subscriptionNamesValidityMessage.setVisible(true);
      subscriptionNamesValidityMessage.setText("A subscription must be selected");
      return;
    }

    int subscriptionIndex = subscriptionNames.getSelectedIndex();
    SubscriptionToCustomer record = subToCustomers.get(subscriptionIndex);

    subToCustController.createRecord(record);
    customerView.setViewportView(customerView.displayRecordListing());
  }
}

