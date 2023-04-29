package elwyn.case_management.models;

public class Subscription extends Record {
  public String name;
  public String description;
  public Frequency frequency;
  public int price;

  public String pricePerPeriod() {
    String val = "£";
    String pounds = Integer.toString(price / 100);
    String pennies = Integer.toString(price % 100);
    if (pennies.length() == 1)
      pennies = "0" + pennies;
    val += pounds + "." + pennies + " / " + frequency.toString();
    return val;
  }
}
