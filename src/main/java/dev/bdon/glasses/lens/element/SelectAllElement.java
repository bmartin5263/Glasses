package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensRuntime;

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
  public String pathComponent() {
    return "[*]";
  }
}
