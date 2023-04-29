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
import elwyn.case_management.controllers.UserController;
import elwyn.case_management.views.CustomerView;
import elwyn.case_management.views.RecordView;

public class UnsubscribeActionListener implements ActionListener {
  CustomerView customerView;
  Long customerId;

  SubscriptionToCustomerController subToCustController;
  SubscriptionController subscriptionController = new SubscriptionController(null);

  JList<String> subscriptionNames;
  List<SubscriptionToCustomer> subToCustomers = new ArrayList<SubscriptionToCustomer>();
  JLabel subscriptionNamesValidityMessage = new JLabel();

  public UnsubscribeActionListener(JPanel panel, Long customerId, CustomerView customerView) {
    this.customerId = customerId;
    this.customerView = customerView;
    subToCustController = new SubscriptionToCustomerController(null, customerId);

    subToCustomers = subToCustController.readRecords(0);

    String[] subscriptionNamesArray = new String[subToCustomers.size()];
    for (int i = 0; i < subToCustomers.size(); i++) {
      subscriptionNamesArray[i] = subscriptionController.readRecord(subToCustomers.get(i).subscription).name;

      subToCustomers.get(i).dateEnded = new Date();
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

    subToCustController.updateRecord(record);
    customerView.setViewportView(customerView.displayRecordListing());
  }
}
