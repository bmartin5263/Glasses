package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;
import dev.bdon.glasses.util.Setter;

import java.util.List;

public non-sealed class MonoLens<I, O> implements Lens<I, O> {
  private final LensContext context;
  private final Class<O> outputType;
  private final Element<Object, O> leaf;

  public MonoLens(LensContext context, Class<O> outputType) {
    this(context, outputType, null);
  }

  public MonoLens(LensContext context, Class<O> outputType, Element<?, O> leaf) {
    this.context = context;
    this.outputType = outputType;
    this.leaf = Element.unchecked(leaf);
  }

  public Image<O> focus(I target) {
    return focusToList(target).get(0);
  }

  @Override
  public List<Image<O>> focusToList(I target) {
    return (List<Image<O>>) (List<?>) LensImpl.focus((Lens<Object, Object>) this, target);
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
  public LensContext context() {
    return context;
  }

  @Override
  public <X> MonoLens<I, X> select(Setter<O, X> setter, Class<X> type) {
    return LensImpl.select(toInternalLens(), setter, type, constructor());
  }

  @Override
  public <X> MonoLens<I, X> selectFirst(Setter<O, List<X>> setter, Class<X> type) {
    return LensImpl.selectFirst(toInternalLens(), setter, type, constructor());
  }

  @Override
  public <X> PolyLens<I, X> selectAll(Setter<O, List<X>> setter, Class<X> type) {
    return LensImpl.selectAll(toInternalLens(), setter, type);
  }

  private InternalLens<O> toInternalLens() {
    return new InternalLens<>(context, leaf, outputType);
  }

  private static <I, O> LensConstructor<I, O, MonoLens<I, O>> constructor() {
    return MonoLens::new;
  }
}
