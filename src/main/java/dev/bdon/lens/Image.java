package dev.bdon.lens;

import java.util.function.Consumer;

public class Image<T> {
  private final T value;
  private final Route route;

  public Image(T value, Route route) {
    this.value = value;
    this.route = route;
  }

  public T value() {
    return value;
  }

  public String path() {
    return route.path();
  }

  public void override(T newValue) {
    throw new LensExecutionException("not implemented");
  }
}
