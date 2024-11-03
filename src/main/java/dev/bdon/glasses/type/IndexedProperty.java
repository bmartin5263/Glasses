package dev.bdon.glasses.type;

import dev.bdon.glasses.util.Assert;

import java.util.List;
import java.util.stream.Stream;

public class IndexedProperty<O> implements IProperty<List<O>, O> {
  private final Class<O> type;
  private final int index;

  public IndexedProperty(Class<O> type, int index) {
    this.type = type;
    this.index = Assert.positiveOrZeroArgument(index, "index");
  }

  @Override
  public O get(List<O> target) {
    Assert.nonNull(target, "Attempting to access index %s on null list", index);
    if (index >= target.size()) {
      return null;
    }
    return target.get(index);
  }

  @Override
  public void set(List<O> target, O newValue) {
    Assert.nonNull(target, "Attempting to access index %s on null list", index);
    if (index >= target.size()) {
      target.add(newValue);
    }
    target.set(index, newValue);
  }

  @Override
  public Class<O> type() {
    return type;
  }

  @SuppressWarnings("unchecked")
  public static <A, B> IndexedProperty<A> unchecked(IndexedProperty<B> property) {
    return (IndexedProperty<A>) property;
  }

  public Stream<Class<?>> getClasses() {
    return Stream.of(type);
  }
}
