package elwyn.case_management.models;

public enum Severity {
  DEBUG("Debug"),
  LOG("Log"),
  WARNING("Warning"),
  ERROR("Error"),
  FATAL("Fatal");

  private final String severity;

  Severity(String severity) {
    this.severity = severity;
  }

  public static Severity parseSelectedSeverity(String severity) {
    if (severity == null)
      return null;
    if (severity.equals(DEBUG.toString()))
      return DEBUG;
    if (severity.equals(LOG.toString()))
      return LOG;
    if (severity.equals(WARNING.toString()))
      return WARNING;
    if (severity.equals(ERROR.toString()))
      return ERROR;
    if (severity.equals(FATAL.toString()))
      return FATAL;
    if (severity.equals(FATAL.toString()))
      return FATAL;
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
    return severity;
  }
}
