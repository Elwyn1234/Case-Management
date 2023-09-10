package elwyn.clinic.models;

public enum VisitationStatus {
  REQUIRES_MEDICATION("Complete Requiring Medication"),
  REFERRED("Referral To Specialist");

  private final String status;

  private VisitationStatus(String status) {
    this.status = status;
  }

  public static VisitationStatus parseSelectedStatus(String status) {
    if (status == null)
      return null;
    if (status.equals(REQUIRES_MEDICATION.toString()))
      return REQUIRES_MEDICATION;
    if (status.equals(REFERRED.toString()))
      return REFERRED;
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
    return status;
  }
}
