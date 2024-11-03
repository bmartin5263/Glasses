package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;
import dev.bdon.glasses.util.Setter;

import java.util.List;

public non-sealed class PolyLens<I, O> implements Lens<I, O> {
  private final LensContext context;
  private final Class<O> outputType;
  private final Element<Object, O> leaf;

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
  public void override(Image<O> image, O newValue) {
    LensImpl.override(toInternalLens(), image, newValue);
  }

  @Override
  public <X> Element<X, O> leaf() {
    return Element.unchecked(leaf);
  }

  @Override
  public Class<O> outputType() {
    return outputType;
  }

  @Override
  public <X> PolyLens<I, X> select(Setter<O, X> setter, Class<X> type) {
    return LensImpl.select(toInternalLens(), setter, type, constructor());
  }

  @Override
  public <X> PolyLens<I, X> selectFirst(Setter<O, List<X>> setter, Class<X> type) {
    return LensImpl.selectFirst(toInternalLens(), setter, type, constructor());
  }

  @Override
  public <X> PolyLens<I, X> selectAll(Setter<O, List<X>> setter, Class<X> type) {
    return LensImpl.selectAll(toInternalLens(), setter, type);
  }

  private InternalLens<O> toInternalLens() {
    return new InternalLens<>(context, leaf, outputType);
  }

  private static <I, O> LensConstructor<I, O, PolyLens<I, O>> constructor() {
    return PolyLens::new;
  }
}
