package elwyn.case_management.models;

public abstract class Record {
  public long id;

  public abstract String toString(int depth);
}
