package dev.bdon.glasses.util;

import java.util.function.BiConsumer;

/**
 * Functional interface representing an object' setter method
 *
 * @param <I> type of object setter is declared
 * @param <O> the type of object being set
 */
public interface Setter<I, O> extends BiConsumer<I, O> {
  default void set(I target, O value) {
    accept(target, value);
  }
}
