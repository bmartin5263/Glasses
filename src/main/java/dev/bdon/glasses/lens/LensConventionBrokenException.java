package dev.bdon.glasses.lens;

public class LensConventionBrokenException extends RuntimeException {
  public LensConventionBrokenException(String message) {
    super(message);
  }

  public LensConventionBrokenException(String message, Object... args) {
    super(message.formatted(args));
  }

  public LensConventionBrokenException(Throwable cause, String message, Object... args) {
    super(message.formatted(args), cause);
  }
}
