package dev.bdon.glasses.lens;

public class LensExecutionException extends RuntimeException {
  public LensExecutionException(String message) {
    super(message);
  }

  public LensExecutionException(String message, Object... args) {
    super(message.formatted(args));
  }

  public LensExecutionException(Throwable cause, String message, Object... args) {
    super(message.formatted(args), cause);
  }
}
