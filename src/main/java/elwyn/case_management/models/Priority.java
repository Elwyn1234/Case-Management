package elwyn.case_management.models;

public enum Priority {
  LOW("Low"),
  MEDIUM("Medium"),
  HIGH("High"),
  URGENT("Urgent");

  private final String priority;

  Priority(String priority) {
    this.priority = priority;
  }

  public static Priority parseSelectedPriority(String rawPriority) {
    if (rawPriority == null)
      return null;
    if (rawPriority == "Low")
      return Priority.LOW;
    if (rawPriority == "Medium")
      return Priority.MEDIUM;
    if (rawPriority == "High")
      return Priority.HIGH;
    if (rawPriority == "Urgent")
      return Priority.URGENT;
    return null;
  }
}
