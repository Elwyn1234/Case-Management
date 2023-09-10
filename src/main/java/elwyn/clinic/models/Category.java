package elwyn.clinic.models;

public enum Category {
  TOPOGRAPHIC("Topographic"),
  ANATOMIC("Anatomic"),
  PHYSIOLOGICAL("Physiological"),
  PATHOLOGICAL("Pathological"),
  ETIOLOGICAL("Etiological"),
  JURISTIC("Juristic"),
  EPIDEMIOLOGICAL("Epidemiological"),
  STATISTICAL("Statistical");

  private final String category;

  Category(String category) {
    this.category = category;
  }

  public static Category parseSelectedCategory(String rawCategory) {
    if (rawCategory == null)
      return null;
    if (rawCategory.equals(TOPOGRAPHIC.toString()))
      return TOPOGRAPHIC;
    if (rawCategory.equals(ANATOMIC.toString()))
      return ANATOMIC;
    if (rawCategory.equals(PHYSIOLOGICAL.toString()))
      return PHYSIOLOGICAL;
    if (rawCategory.equals(PATHOLOGICAL.toString()))
      return PATHOLOGICAL;
    if (rawCategory.equals(ETIOLOGICAL.toString()))
      return ETIOLOGICAL;
    if (rawCategory.equals(JURISTIC.toString()))
      return JURISTIC;
    if (rawCategory.equals(EPIDEMIOLOGICAL.toString()))
      return EPIDEMIOLOGICAL;
    if (rawCategory.equals(STATISTICAL.toString()))
      return STATISTICAL;
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
    return category;
  }
}
