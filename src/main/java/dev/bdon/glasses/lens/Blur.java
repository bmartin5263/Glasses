package dev.bdon.glasses.lens;

import dev.bdon.glasses.path.Path;
import dev.bdon.glasses.util.Assert;

public class Blur<T> {
  private final Lens<?, ?> lens;
  private final T value;
  private final Path path;
  private final boolean dead;

  public Blur(Lens<?, ?> lens, T value, Path path) {
    this(lens, value, path, false);
  }

  public Blur(Lens<?, ?> lens, T value, Path path, boolean dead) {
    this.lens = Assert.nonNullArgument(lens, "lens");
    this.value = Assert.nonNull(value, "value cannot be null. Use dead=true and a non-null value to represent null");
    this.path = Assert.nonNullArgument(path, "route");
    this.dead = dead;
  }

  public static <T> Blur<T> root(Lens<?, ?> lens, T value) {
    return new Blur<>(lens, value, new Path(), false);
  }

  public Image<T> toImage(Object root) {
    Assert.nonNull(value, "value should not be null");
    return new Image<>(root, dead ? null : value, path, lens);
  }

  public T value() {
    return value;
  }

  public Lens<?, ?> lens() {
    return lens;
  }

  public Path path() {
    return path;
  }

  public boolean isDead() {
    return dead;
  }
}
