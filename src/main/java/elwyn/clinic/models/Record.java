package elwyn.clinic.models;

public abstract class Record {
  public long id;

  public abstract String toString(int depth);
}
