package dev.bdon.glasses.util;

import dev.bdon.glasses.lens.LensInternalException;

import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("UnusedReturnValue")
public class Assert {
  private Assert() {}

  public static <T> T nonNull(T obj) {
    return Objects.requireNonNull(obj);
  }

  public static <T> T nonNull(T obj, String format, Object... args) {
    return Objects.requireNonNull(obj, format.formatted(args));
  }

  public static <T, E extends RuntimeException> T nonNull(T obj, Function<String, E> exceptionFactory, String format, Object... args) {
    if (obj == null) {
      throw exceptionFactory.apply(format.formatted(args));
    }
    return obj;
  }

  public static <T> T nonNullArgument(T obj, String name) {
    return Objects.requireNonNull(obj, "%s must not be null".formatted(name));
  }


  public static int between(int subject, int minInclusive, int maxExclusive, String format, Object... args) {
    if (subject >= minInclusive && subject < maxExclusive) {
      return subject;
    }
    throw new LensInternalException(format, args);
  }

  public static <T, E extends RuntimeException> T nonNullArgument(T obj, String name, Function<String, E> exceptionFactory) {
    if (obj == null) {
      throw exceptionFactory.apply("%s must not be null".formatted(name));
    }
    return obj;
  }

  public static int positiveOrZeroArgument(int value, String name) {
    if (value < 0) {
      throw new LensInternalException("%s must be greater than or equal to zero: %s", name, value);
    }
    return value;
  }
}
