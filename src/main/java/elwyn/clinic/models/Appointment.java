package elwyn.clinic.models;

import java.util.Date;

public class Appointment extends Record {
  public String summary;
  public String description;
  public String pharmacy;
  public Customer customer;
  public User createdBy;
  public Date date;
  public Date dateCreated;
  public VisitationStatus visitationStatus;
  public String closed;
  public User assignedTo;
  public User referredTo;
  public int prescribedHourlyFrequency;
  public int prescribedMgDose;
  public String prescribedMedication;

  public String toString(int depth) {
    String indent = "    ";
    for (int i = 0; i < depth; i++) {
      indent += indent;
    }
    String val = "{\n";
    val += indent + "id: " + id + "\n";
    val += indent + "date: " + date + "\n";
    val += indent + "summary: " + summary + "\n";
    val += indent + "description: " + description + "\n";
    val += indent + "customer: " + customer.id + "\n";
    val += indent + "createdBy: " + createdBy.username + "\n";
    val += indent + "referredTo: " + referredTo.username + "\n";
    val += indent + "date: " + date + "\n";
    val += "}";
    return val;
  }
}
