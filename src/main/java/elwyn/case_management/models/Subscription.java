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

  @Override
  public String toString(int depth) {
    String indent = "    ";
    for (int i = 0; i < depth; i++) {
      indent += indent;
    }
    String val = "{\n";
    val += indent + "id: " + id + "\n";
    val += indent + "name: " + name + "\n";
    val += indent + "description: " + description + "\n";
    val += indent + "frequency: " + frequency + "\n";
    val += indent + "price: " + price + "\n";
    val += "}";
    return val;
  }
}
