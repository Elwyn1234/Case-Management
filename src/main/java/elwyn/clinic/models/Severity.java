package elwyn.clinic.models;

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

  public static Severity parseSelectedSeverity(String rawSeverity) {
    if (rawSeverity == null)
      return null;
    if (rawSeverity.equals(DEBUG.toString()))
      return DEBUG;
    if (rawSeverity.equals(LOG.toString()))
      return LOG;
    if (rawSeverity.equals(WARNING.toString()))
      return WARNING;
    if (rawSeverity.equals(ERROR.toString()))
      return ERROR;
    if (rawSeverity.equals(FATAL.toString()))
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
