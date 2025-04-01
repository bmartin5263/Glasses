package dev.bdon.glasses.util;

import java.util.function.Function;

/**
 * Utility class for pre-conditions, post-conditions, invariants, etc.
 *
 * Note most methods do not delegate to another. This is to help keep the stack trace simple with fewer extra
 * stack frames.
 */
@SuppressWarnings("UnusedReturnValue")
public class Assert {
  private Assert() {}

  public static <T> T nonNull(T obj, String message, Object... args) {
    if (obj == null) {
      throw new NullPointerException(message.formatted(args));
    }
    return obj;
  }

  public static <T, E extends RuntimeException> T nonNull(T obj, Function<String, E> exceptionFactory, String message, Object... args) {
    if (obj == null) {
      throw exceptionFactory.apply(message.formatted(args));
    }
    return obj;
  }

  public static <T> T nonNullArgument(T obj, String name) {
    if (obj == null) {
      throw new NullPointerException("%s must not be null".formatted(name));
    }
    return obj;
  }


  public static <T, E extends RuntimeException> T nonNullArgument(T obj, Function<String, E> exceptionFactory, String name) {
    if (obj == null) {
      throw exceptionFactory.apply("%s must not be null".formatted(name));
    }
    return obj;
  }

  public static int nonNegativeArgument(int value, String name) {
    if (value < 0) {
      throw new LensInternalException("%s must be greater than or equal to zero: %s", name, value);
    }
    return value;
  }
}
