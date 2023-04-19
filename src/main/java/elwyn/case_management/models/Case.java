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
    if (status.equals("Closed")) {
      status += " (" + dateClosed + ")";
    }
    return status;
  }
}
