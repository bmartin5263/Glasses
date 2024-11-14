package dev.bdon.glasses.type;

import java.util.stream.Stream;

public interface Property<I, O> {
  O get(I target);
  void set(I target, O newValue);
  Class<O> type();
  Stream<Class<?>> getClasses();

  @SuppressWarnings("unchecked")
  static <A, B, C, D> Property<A, B> unchecked(Property<C, D> property) {
    return (Property<A, B>) property;
  }
}
