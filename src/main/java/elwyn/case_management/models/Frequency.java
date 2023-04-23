package elwyn.case_management.models;

public enum Frequency {
  DAILY("Daily"),
  WEEKLY("Weekly"),
  MONTHLY("Monthly"),
  ANNUALLY("Annually");

  private final String frequency;

  Frequency(String frequency) {
    this.frequency = frequency;
  }

  public static Frequency parseSelectedFrequency(String frequency) {
    if (frequency == null)
      return null;
    if (frequency.equals(DAILY.toString()))
      return DAILY;
    if (frequency.equals(WEEKLY.toString()))
      return WEEKLY;
    if (frequency.equals(MONTHLY.toString()))
      return MONTHLY;
    if (frequency.equals(ANNUALLY.toString()))
      return ANNUALLY;
    return null;
  }

  public static String[] stringValues() {
    String[] array = new String[values().length];
    for (int i = 0; i < values().length; i++) {
      array[i] = values()[i].toString();
    }
    return array;
  }

  @Override
  public String toString() {
    return frequency;
  }
}
