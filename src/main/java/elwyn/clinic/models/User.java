package elwyn.clinic.models;

import java.util.ArrayList;
import java.util.List;

public class User extends Record {
  public String name;
  public String username;
  public String password;
  public Role role;
  public Long teamLead;

  public String fullNameAndId() { 
    return name + " (" + id + ")";
  }

  public List<Appointment> selectMyAppointments(List<Appointment> appointments) {
    List<Appointment> filteredAppointments = new ArrayList<Appointment>();
    for (Appointment appointmentRecord : appointments) {
      if (appointmentRecord == null || appointmentRecord.assignedTo == null)
        continue;
      if (appointmentRecord.assignedTo.id == id)
        filteredAppointments.add(appointmentRecord);
    }
    return filteredAppointments;
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
    val += indent2 + "name: " + name + "\n";
    val += indent2 + "username: " + username + "\n";
    val += indent2 + "password: " + "Value hidden" + "\n";
    val += indent2 + "role: " + role + "\n";
    val += indent2 + "teamLead: " + teamLead + "\n";
    val += indent1 + "}";
    return val;
  }
}
