package elwyn.case_management.models;

public class Contact extends Record {
  public String description;
  public String date;
  public String time;
  public ContactMethod contactMethod;
  public Case caseRecord;
}
