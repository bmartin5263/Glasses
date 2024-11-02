package dev.bdon.lens;

import java.util.Objects;
import java.util.function.Function;

public class Assert {

  public static <T> T nonNull(T obj) {
    return Objects.requireNonNull(obj);
  }

  public static <T> T nonNull(T obj, String name) {
    return Objects.requireNonNull(obj, "%s must not be null".formatted(name));
  }

  public static <T, E extends RuntimeException> T nonNull(T obj, String name, Function<String, E> exceptionFactory) {
    if (obj == null) {
      throw exceptionFactory.apply("%s must not be null".formatted(name));
    }
    return obj;
  }

  public static <T, E extends RuntimeException> T nonNull(T obj, Function<String, E> exceptionFactory, String format, Object... args) {
    if (obj == null) {
      throw exceptionFactory.apply(format.formatted(args));
    }
    return obj;
  }

}
