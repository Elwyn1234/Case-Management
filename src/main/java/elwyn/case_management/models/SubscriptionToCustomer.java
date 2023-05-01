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
    String oneIndent = "        ";
    String indent1 = "";
    String indent2 = oneIndent;
    for (int i = 0; i < depth; i++) {
      indent1 += oneIndent;
      indent2 += oneIndent;
    }
    String val = "{\n";
    if (id != 0)
      val += indent2 + "id: " + id + "\n";
    val += indent2 + "subscription: " + subscription + "\n";
    val += indent2 + "customer: " + customer + "\n";
    val += indent2 + "dateStarted: " + dateStarted + "\n";
    val += indent2 + "dateEnded: " + dateEnded + "\n";
    val += indent1 + "}";
    return val;
  }
}
