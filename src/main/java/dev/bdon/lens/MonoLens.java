package dev.bdon.lens;

import java.util.List;

public non-sealed class MonoLens<I, O> implements Lens<I, O> {
  private final Class<O> outputType;
  private final Element<Object, Object> leaf;

  public MonoLens(Class<O> outputType) {
    this(outputType, null);
  }

  public <X> MonoLens(Class<O> outputType, Element<X, O> leaf) {
    this.outputType = outputType;
    this.leaf = (Element<Object, Object>) leaf;
  }

  public Image<O> focus(I target) {
    return focusToList(target).get(0);
  }

  @Override
  public List<Image<O>> focusToList(I target) {
    return (List<Image<O>>) (List<?>) LensImpl.focus((Lens<Object, Object>) this, target);
  }

  @Override
  public Element<?, O> leaf() {
    return (Element<?, O>) leaf;
  }

  @Override
  public <X> MonoLens<I, X> select(Getter<O, X> getter, Setter<O, X> setter, Class<X> type) {
    var next = new SelectElement<>(leaf, getter, setter, outputType, type);
    return new MonoLens<>(type, next);
  }

  @Override
  public <X, L extends List<X>> MonoLens<I, X> selectFirst(Getter<O, L> getter) {
    return null;
  }

  @Override
  public <X> PolyLens<I, X> selectAll(Getter<O, List<X>> getter, Class<X> type) {
    List<X> x = List.of();

    var next = new SelectAllElement<List<X>, X>(new SelectListElement<>(leaf, getter, LensUtils.getTracer(outputType)));
    return new PolyLens<>(type, next);
  }
}
