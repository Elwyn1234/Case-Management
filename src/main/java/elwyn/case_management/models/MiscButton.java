package elwyn.case_management.models;

import java.util.function.Consumer;
import java.util.function.Function;

public class MiscButton <T extends Record> {
  public Consumer<Long> buttonPressed;
  public Function<T, Boolean> shouldShowButton;
  public String buttonText;

  public MiscButton(Consumer<Long> buttonPressed, Function<T, Boolean> shouldShowButton, String buttonText) {
    this.buttonPressed = buttonPressed;
    this.shouldShowButton = shouldShowButton;
    this.buttonText = buttonText;
  }
}
