package elwyn.clinic.models;

import java.util.Date;

public class Log extends Record {
  public Long user;
  public Severity severity;
  public String log;
  public Date date;

  public String toString(int depth) {
    String indent = "    ";
    for (int i = 0; i < depth; i++) {
      indent += indent;
    }
    String val = "{\n";
    val += indent + "id: " + id + "\n";
    val += indent + "user: " + user + "\n";
    val += indent + "severity: " + severity + "\n";
    val += indent + "log: " + log + "\n";
    val += indent + "date: " + date + "\n";
    val += "}";
    return val;
  }
}
