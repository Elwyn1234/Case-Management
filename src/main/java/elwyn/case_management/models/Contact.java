package elwyn.case_management.models;

import java.util.Date;

public class Contact extends Record {
  public String description;
  public Date date;
  public ContactMethod contactMethod;
  public Case caseRecord;
  public User user;
}
