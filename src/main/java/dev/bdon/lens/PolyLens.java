package dev.bdon.lens;

import java.util.List;
import java.util.function.Supplier;

public non-sealed class PolyLens<I, O> implements Lens<I, O> {
  private final LensContext context;
  private final Class<O> outputType;
  private final Element<Object, Object> leaf;

  public PolyLens(LensContext context, Class<O> outputType, Element<?, O> leaf) {
    this.context = context;
    this.outputType = outputType;
    this.leaf = Element.unchecked(leaf);
  }

  public List<Image<O>> focus(I target) {
    return (List<Image<O>>) (List<?>) LensImpl.focus((Lens<Object, Object>) this, target);
  }

  @Override
  public LensContext context() {
    return context;
  }

  @Override
  public List<Image<O>> focusToList(I target) {
    return focus(target);
  }

  @Override
  public <X> Element<X, O> leaf() {
    return Element.unchecked(leaf);
  }

  @Override
  public <X> PolyLens<I, X> select(Setter<O, X> setter, Class<X> type) {
    var tracer = LensUtils.newTracer(outputType);
    var property = context.findProperty(tracer, setter, type);
    var next = new SelectElement<>(leaf, property);
    return new PolyLens<>(context, type, next);
  }

  @Override
  public <X, L extends List<X>> PolyLens<I, X> selectFirst(Getter<O, L> getter) {
    return null;
  }

  @Override
  public <X> PolyLens<I, X> selectAll(Setter<O, List<X>> getter, Class<X> type) {
    return null;
  }
}
