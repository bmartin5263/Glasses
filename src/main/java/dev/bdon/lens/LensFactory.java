package dev.bdon.lens;

public class LensFactory {
  public static LensFactory DEFAULT = new LensFactory();

  public <T> MonoLens<T, T> create(Class<T> type) {
    return new MonoLens<>(type);
  }
}
