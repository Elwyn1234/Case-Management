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
    if (rawPriority.equals(LOW.toString()))
      return LOW;
    if (rawPriority.equals(MEDIUM.toString()))
      return MEDIUM;
    if (rawPriority.equals(HIGH.toString()))
      return HIGH;
    if (rawPriority.equals(URGENT.toString()))
      return URGENT;
    return null;
  }

  @Override
  public String toString() {
    return priority;
  }
}
