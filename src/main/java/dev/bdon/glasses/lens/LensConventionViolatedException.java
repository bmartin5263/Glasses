package dev.bdon.glasses.lens;

public class LensConventionViolatedException extends RuntimeException {
  public LensConventionViolatedException(String message) {
    super(message);
  }

  public LensConventionViolatedException(String message, Object... args) {
    super(message.formatted(args));
  }

  public LensConventionViolatedException(Throwable cause, String message, Object... args) {
    super(message.formatted(args), cause);
  }
}
