package elwyn.case_management.models;

import java.util.Date;

public class Case extends Record {
  public String summary;
  public String description;
  public Customer customer;
  public User createdBy;
  public User assignedTo;
  public Date dateOpened;
  public Date dateClosed;
  public Priority priority;
  // public List<Contact> contacts

  public String getStatus() {
    String status = dateClosed == null ? "Open" : "Closed";
    return status;
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
    val += indent2 + "summary: " + summary + "\n";
    val += indent2 + "description: " + description + "\n";

    if (customer == null)
      val += indent2 + "customer: null\n";
    else
      val += indent2 + "customer: " + customer.toString(depth + 1) + "\n";

    if (createdBy == null)
      val += indent2 + "createdBy: null\n";
    else
      val += indent2 + "createdBy: " + createdBy.toString(depth + 1) + "\n";

    if (assignedTo == null)
      val += indent2 + "assignedTo: null\n";
    else
      val += indent2 + "assignedTo: " + assignedTo.toString(depth + 1) + "\n";

    val += indent2 + "dateOpened: " + dateOpened + "\n";
    val += indent2 + "dateClosed: " + dateClosed + "\n";
    val += indent2 + "priority: " + priority + "\n";
    val += indent1 + "}";
    return val;
  }
}
