package dev.bdon.lens;

import java.util.ArrayList;
import java.util.List;

public non-sealed class MonoLens<I, O> implements Lens<I, O> {
  private final LensContext context;
  private final Class<O> outputType;
  private final Element<Object, O> leaf;

  public MonoLens(LensContext context, Class<O> outputType) {
    this(context, outputType, null);
  }

  public <X> MonoLens(LensContext context, Class<O> outputType, Element<X, O> leaf) {
    this.context = context;
    this.outputType = outputType;
    this.leaf = (Element<Object, O>) leaf;
  }

  public Image<O> focus(I target) {
    return focusToList(target).get(0);
  }

  @Override
  public List<Image<O>> focusToList(I target) {
    return (List<Image<O>>) (List<?>) LensImpl.focus((Lens<Object, Object>) this, target);
  }

  @Override
  public <X> Element<X, O> leaf() {
    return Element.unchecked(leaf);
  }

  @Override
  public LensContext context() {
    return context;
  }

  @Override
  public <X> MonoLens<I, X> select(Setter<O, X> setter, Class<X> type) {
    var target = LensUtils.newTracer(outputType);
    var property = context.findProperty(target, setter, type);
    var next = new SelectElement<>(leaf, property);
    return new MonoLens<>(context, type, next);
  }

  @Override
  public <X, L extends List<X>> MonoLens<I, X> selectFirst(Getter<O, L> getter) {
    return null;
  }

  @Override
  public <X> PolyLens<I, X> selectAll(Setter<O, List<X>> setter, Class<X> type) {
    var target = LensUtils.newTracer(outputType);
    var listType = (Class<List<X>>)(Class<?>)List.class;
    Property<O, List<X>> property = context.findProperty(target, setter, listType);
    var next = new SelectAllElement<List<X>, X>(new SelectListElement<>(leaf, property));
    return new PolyLens<>(context, type, next);
  }
}
