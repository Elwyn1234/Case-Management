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
    String indent = "    ";
    for (int i = 0; i < depth; i++) {
      indent += indent;
    }
    String val = "{\n";
    val += indent + "id: " + id + "\n";
    val += indent + "summary: " + summary + "\n";
    val += indent + "description: " + description + "\n";

    if (customer == null)
      val += indent + "customer: null\n";
    else
      val += indent + "customer: " + customer.toString(depth + 1) + "\n";

    if (createdBy == null)
      val += indent + "createdBy: null\n";
    else
      val += indent + "createdBy: " + createdBy.toString(depth + 1) + "\n";

    if (assignedTo == null)
      val += indent + "assignedTo: null\n";
    else
      val += indent + "assignedTo: " + assignedTo.toString(depth + 1) + "\n";

    val += indent + "dateOpened: " + dateOpened + "\n";
    val += indent + "dateClosed: " + dateClosed + "\n";
    val += indent + "priority: " + priority + "\n";
    val += "}";
    return val;
  }
}
