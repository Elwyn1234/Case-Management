package elwyn.case_management.models;

public class Subscription extends Record {
  public Customer customer;
  public SubscriptionType subscriptionType;
  public String dateStarted;
  public int days;
}
