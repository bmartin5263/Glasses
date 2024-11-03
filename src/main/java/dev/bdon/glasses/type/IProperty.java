package dev.bdon.glasses.type;

import java.util.stream.Stream;

public interface IProperty<I, O> {
  O get(I target);
  void set(I target, O newValue);
  Class<O> type();
  Stream<Class<?>> getClasses();
}