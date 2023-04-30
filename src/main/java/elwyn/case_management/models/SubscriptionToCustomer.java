package elwyn.case_management.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubscriptionToCustomer extends Record {
  public Long subscription;
  public Long customer; // eTODO: hashed?
  public Date dateStarted;
  public Date dateEnded;

  public List<SubscriptionToCustomer> selectCustomerSubscriptions(List<SubscriptionToCustomer> records, Long customerId) {
    List<SubscriptionToCustomer> filteredSubscriptionToCustomer = new ArrayList<SubscriptionToCustomer>();
    for (SubscriptionToCustomer subscriptionToCustomer : records) {
      if (subscriptionToCustomer == null || subscriptionToCustomer.customer == null)
        continue;
      if (subscriptionToCustomer.customer.equals(customerId))
        filteredSubscriptionToCustomer.add(subscriptionToCustomer);
    }
    return filteredSubscriptionToCustomer;
  }

  public String toString(int depth) {
    String indent = "    ";
    for (int i = 0; i < depth; i++) {
      indent += indent;
    }
    String val = "{\n";
    val += indent + "id: " + id + "\n";
    val += indent + "subscription: " + subscription + "\n";
    val += indent + "customer: " + customer + "\n";
    val += indent + "dateStarted: " + dateStarted + "\n";
    val += indent + "dateEnded: " + dateEnded + "\n";
    val += "}";
    return val;
  }
}
