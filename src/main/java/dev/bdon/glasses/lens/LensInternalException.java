package dev.bdon.glasses.lens;

public class LensInternalException extends RuntimeException {
  public LensInternalException(String message) {
    super(message);
  }

  public LensInternalException(String message, Object... args) {
    super(message.formatted(args));
  }

  public LensInternalException(Throwable cause, String message, Object... args) {
    super(message.formatted(args), cause);
  }
}
