package elwyn.case_management.models;

public enum SubscriptionType {
  PERSONAL("Personal"),
  BUSINESS("Business"),
  ENTERPRISE("Enterprise");

  private final String subscriptionType;

  SubscriptionType(String subscriptionType) {
    this.subscriptionType = subscriptionType;
  }

  public static SubscriptionType parseSelectedSubscriptionType(String subscriptionType) {
    if (subscriptionType == null)
      return null;
    if (subscriptionType.equals(PERSONAL.toString()))
      return PERSONAL;
    if (subscriptionType.equals(BUSINESS.toString()))
      return BUSINESS;
    if (subscriptionType.equals(ENTERPRISE.toString()))
      return ENTERPRISE;
    return null;
  }

  public static String[] stringValues() {
    String[] array = new String[values().length];
    for (int i = 0; i < values().length; i++) {
      array[i] = values()[i].toString();
    }
    return array;
  }

  @Override
  public String toString() {
    return subscriptionType;
  }
}
