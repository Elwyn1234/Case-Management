package elwyn.clinic.models;

public class Medicine extends Record {
  public String name;
  public String description;

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
    val += indent2 + "name: " + name + "\n";
    val += indent2 + "description: " + description + "\n";
    val += indent1 + "}";
    return val;
  }
}
