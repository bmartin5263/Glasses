package dev.bdon.glasses.util;

import java.util.function.Function;

/**
 * Utility class for pre-conditions, post-conditions, invariants, etc.
 *
 * Note most methods do not delegate to another. This is to help keep the stack trace simple with fewer frames
 */
@SuppressWarnings("UnusedReturnValue")
public class Assert {
  private Assert() {}

  /**
   * Asserts a value is non-null
   *
   * @param value the value to test
   * @param message error message format
   * @param args format args
   * @param <T> type of object
   *
   * @return the object
   * @throws LensInternalException if the value is null
   */
  public static <T> T nonNull(T value, String message, Object... args) {
    if (value == null) {
      throw new LensInternalException(message.formatted(args));
    }
    return value;
  }

  /**
   * Asserts a value is non-null
   *
   * @param value the value to test
   * @param exceptionFactory function to convert error message into exception (i.e. a constructor)
   * @param message error message format
   * @param args format args
   * @param <T> type of object
   * @param <E> type of exception to throw
   *
   * @return the object
   * @throws E if the value is null
   */
  public static <T, E extends RuntimeException> T nonNull(T value, Function<String, E> exceptionFactory, String message, Object... args) {
    if (value == null) {
      throw exceptionFactory.apply(message.formatted(args));
    }
    return value;
  }

  /**
   * Asserts a value is non-null. Uses a preformatted error message meant for validating method arguments
   * validation
   *
   * @param value the value to test
   * @param name parameter name
   * @param <T> type of object
   *
   * @return the object
   * @throws LensInternalException if the value is null
   */
  public static <T> T nonNullArgument(T value, String name) {
    if (value == null) {
      throw new LensInternalException("%s must not be null".formatted(name));
    }
    return value;
  }

  /**
   * Asserts a value is non-null. Uses a preformatted error message meant for validating method arguments
   * validation
   *
   * @param value the value to test
   * @param exceptionFactory function to convert error message into exception (i.e. a constructor)
   * @param name parameter name
   * @param <T> type of object
   *
   * @return the object
   * @throws E if the value is null
   */
  public static <T, E extends RuntimeException> T nonNullArgument(T value, Function<String, E> exceptionFactory, String name) {
    if (value == null) {
      throw exceptionFactory.apply("%s must not be null".formatted(name));
    }
    return value;
  }

  /**
   * Asserts an integer is non-negative. Uses a preformatted error message meant for validating method arguments
   * validation
   *
   * @param value the value to test
   * @param name parameter name
   *
   * @return the value
   * @throws LensInternalException if the value is neaative
   */
  public static int nonNegativeArgument(int value, String name) {
    if (value < 0) {
      throw new LensInternalException("%s must be greater than or equal to zero: %s", name, value);
    }
    return value;
  }
}
