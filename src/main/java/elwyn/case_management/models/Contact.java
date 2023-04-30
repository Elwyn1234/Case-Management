package elwyn.case_management.models;

import java.util.Date;

public class Contact extends Record {
  public String description;
  public Date date;
  public ContactMethod contactMethod;
  public Case caseRecord;
  public User user;

  @Override
  public String toString(int depth) {
    String indent = "    ";
    for (int i = 0; i < depth; i++) {
      indent += indent;
    }
    String val = "{\n";
    val += indent + "id: " + id + "\n";
    val += indent + "description: " + description + "\n";
    val += indent + "date: " + date + "\n";
    val += indent + "contactMethod: " + contactMethod + "\n";
    
    if (caseRecord == null)
      val += indent + "caseRecord: null\n";
    else
      val += indent + "caseRecord: " + caseRecord + "\n";

    if (user == null)
      val += indent + "user: null\n";
    else
      val += indent + "user: " + user + "\n";

    val += "}";
    return val;
  }
}
