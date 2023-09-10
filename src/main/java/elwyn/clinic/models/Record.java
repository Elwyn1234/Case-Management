package elwyn.clinic.models;

public abstract class Record {
  public long id;
  public boolean showDeleteButton = true;
  public boolean showUpdateButton = true;

  public abstract String toString(int depth);
}
