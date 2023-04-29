package elwyn.case_management.models;

import java.util.function.Function;

import javax.swing.JComponent;

public class MiscButton <T extends Record> {
  public Function<Long, JComponent> buttonPressed;
  public Function<T, Boolean> shouldShowButton;
  public String buttonText;

  public MiscButton(Function<Long, JComponent>  buttonPressed, Function<T, Boolean> shouldShowButton, String buttonText) {
    this.buttonPressed = buttonPressed;
    this.shouldShowButton = shouldShowButton;
    this.buttonText = buttonText;
  }
}
