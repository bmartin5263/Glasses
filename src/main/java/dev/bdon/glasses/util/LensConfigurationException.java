package dev.bdon.glasses.util;

public class LensConfigurationException extends RuntimeException {
  public LensConfigurationException(String message, Object... args) {
    super(message.formatted(args));
  }

  public LensConfigurationException(Throwable cause, String message, Object... args) {
    super(message.formatted(args), cause);
  }
}
