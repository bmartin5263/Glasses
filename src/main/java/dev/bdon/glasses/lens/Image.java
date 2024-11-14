package dev.bdon.glasses.lens;

import dev.bdon.glasses.path.Path;
import dev.bdon.glasses.util.Assert;

public class Image<T> {
  private final Object target;
  private final T value;
  private final Path path;
  private final Lens<?, T> lens;

  public Image(Object target, T value, Path path, Lens<?, ?> lens) {
    this.target = Assert.nonNullArgument(target, "target");
    this.value = value;
    this.path = Assert.nonNullArgument(path, "route");
    this.lens = Assert.nonNullArgument(LensImpl.unchecked(lens), "lens");
  }

  public T value() {
    return value;
  }

  public Path path() {
    return path;
  }

  public Object target() {
    return target;
  }

  public Path route() {
    return path;
  }

  public void override(T newValue) {
    lens.override(this, newValue);
  }
}
