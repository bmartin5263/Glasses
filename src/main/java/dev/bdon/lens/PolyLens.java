package dev.bdon.lens;

import java.util.List;
import java.util.function.Supplier;

public non-sealed class PolyLens<I, O> implements Lens<I, O> {
  private final Class<O> outputType;
  private final Element<Object, Object> leaf;

  public PolyLens(Class<O> outputType, Element<?, O> leaf) {
    this.outputType = outputType;
    this.leaf = (Element<Object, Object>) leaf;
  }

  public List<Image<O>> focus(I target) {
    return (List<Image<O>>) (List<?>) LensImpl.focus((Lens<Object, Object>) this, target);
  }

  @Override
  public List<Image<O>> focusToList(I target) {
    return focus(target);
  }

  @Override
  public Element<?, O> leaf() {
    return (Element<?, O>) leaf;
  }

  @Override
  public <X> PolyLens<I, X> select(Getter<O, X> getter, Setter<O, X> setter, Class<X> type) {
    var next = new SelectElement<>(leaf, getter, setter, outputType, type);
    return new PolyLens<>(type, next);
  }

  @Override
  public <X, L extends List<X>> PolyLens<I, X> selectFirst(Getter<O, L> getter) {
    return null;
  }

  @Override
  public <X> PolyLens<I, X> selectAll(Getter<O, List<X>> getter, Class<X> type) {
    return null;
  }
}
