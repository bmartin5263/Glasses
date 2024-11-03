package dev.bdon.lens;

import java.util.List;

public class SelectAllElement<I, O> extends Element<List<I>, O> {

  public <X, Y> SelectAllElement(Element<X, Y> parent) {
    super(parent);
  }

  @Override
  public Blurs<O> apply(LensRuntime runtime, Blur<List<I>> blur) {
    return blur.divide();
  }

  @Override
  public O overwrite(List<I> input, O newValue) {
    return null;
  }

  @Override
  public O overwriteWithDefaultIfNull(List<I> input) {
    return null;
  }

  @Override
  public String pathComponent() {
    return "[*]";
  }
}
