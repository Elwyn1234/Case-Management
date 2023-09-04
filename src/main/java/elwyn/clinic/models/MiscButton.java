package elwyn.clinic.models;

import java.util.function.Function;

import java.awt.Dimension;

import javax.swing.JComponent;

public class MiscButton <T extends Record> {
  public Function<Long, JComponent> buttonPressed;
  public Function<T, Boolean> shouldShowButton;
  public String buttonText;
  public Dimension buttonDimension;

  public MiscButton(Function<Long, JComponent>  buttonPressed, Function<T, Boolean> shouldShowButton, String buttonText, Dimension buttonDimension) {
    this.buttonPressed = buttonPressed;
    this.shouldShowButton = shouldShowButton;
    this.buttonText = buttonText;
    this.buttonDimension = buttonDimension;
  }
}
