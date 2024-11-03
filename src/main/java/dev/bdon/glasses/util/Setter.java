package dev.bdon.glasses.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Setter<I, O> extends BiConsumer<I, O> {
  default void set(I target, O value) {
    accept(target, value);
  }
}
