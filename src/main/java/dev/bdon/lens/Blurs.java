package dev.bdon.lens;

import java.util.*;

public class Blurs<T> extends AbstractList<Blur<T>> {
  private final List<Blur<T>> delegate;

  public Blurs() {
    this.delegate = new ArrayList<>();
  }

  public Blurs(Collection<Blur<T>> delegate) {
    this.delegate = new ArrayList<>(Objects.requireNonNull(delegate, "Blurs list cannot be null"));
  }

  public Blurs(int initialCapacity) {
    this.delegate = new ArrayList<>(initialCapacity);
  }

  public static <T> Blurs<T> of(Blur<T> blur) {
    return new Blurs<>(List.of(blur));
  }

  @Override
  public Blur<T> get(int index) {
    return delegate.get(index);
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public boolean add(Blur<T> blur) {
    return delegate.add(blur);
  }

  @Override
  public void add(int index, Blur<T> blur) {
    delegate.add(index, blur);
  }

  @Override
  public boolean addAll(int index, Collection<? extends Blur<T>> c) {
    return delegate.addAll(index, c);
  }

  @Override
  public Blur<T> set(int index, Blur<T> element) {
    return delegate.set(index, element);
  }
}
