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
    val += indent2 + "name: " + name + "\n";
    val += indent2 + "description: " + description + "\n";
    val += indent2 + "frequency: " + frequency + "\n";
    val += indent2 + "price: " + price + "\n";
    val += indent1 + "}";
    return val;
  }
}
