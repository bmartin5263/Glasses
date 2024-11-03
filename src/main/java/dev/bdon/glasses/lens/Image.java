package dev.bdon.glasses.lens;

import dev.bdon.glasses.util.Assert;

public class Image<T> {
  private final Object target;
  private final T value;
  private final Route route;
  private final Lens<?, T> lens;

  public Image(Object target, T value, Route route, Lens<?, ?> lens) {
    this.target = Assert.nonNullArgument(target, "target");
    this.value = value;
    this.route = Assert.nonNullArgument(route, "route");
    this.lens = Assert.nonNullArgument((Lens<?, T>) lens, "lens");
  }

  public T value() {
    return value;
  }

  public String path() {
    return route.path();
  }

  public Object target() {
    return target;
  }

  public Route route() {
    return route;
  }

  public void override(T newValue) {
    lens.override(this, newValue);
  }
}
