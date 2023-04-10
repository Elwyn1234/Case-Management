package elwyn.case_management.models;

public class Case extends Record {
  public String summary;
  public String description;
  public Customer customer;
  public User user;
  public String dateOpened;
  public String dateClosed;
  public Priority priority;
  // public List<Contact> contacts
}
