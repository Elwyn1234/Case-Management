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
    String oneIndent = "        ";
    String indent1 = "";
    String indent2 = oneIndent;
    for (int i = 0; i < depth; i++) {
      indent1 += oneIndent;
      indent2 += oneIndent;
    }
    String val = "{\n";
    if (id != 0)
      val += indent2 + "id: " + id + "\n"; // eTODO: use Long, not long
    val += indent2 + "description: " + description + "\n";
    val += indent2 + "date: " + date + "\n";
    val += indent2 + "contactMethod: " + contactMethod + "\n";
    
    if (caseRecord == null)
      val += indent2 + "caseRecord: null\n";
    else
      val += indent2 + "caseRecord: " + caseRecord.toString(depth + 1) + "\n";

    if (user == null)
      val += indent2 + "user: null\n";
    else
      val += indent2 + "user: " + user.toString(depth + 1) + "\n";

    val += indent1 + "}";
    return val;
  }
}
