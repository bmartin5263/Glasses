package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.*;
import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.util.Getter;
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
  public Class<O> outputType() {
    return outputType;
  }

  @Override
  public LensContext context() {
    return context;
  }

  @Override
  public <X> MonoLens<I, X> select(Setter<O, X> setter, Class<X> type) {
    return LensImpl.select(new InternalLens<>(context, leaf, outputType), setter, type, constructor());
  }

  @Override
  public <X> MonoLens<I, X> selectFirst(Setter<O, List<X>> setter, Class<X> type) {
    var target = LensUtils.newTracer(outputType);
    var listClass = (Class<List<X>>)(Class<?>)List.class;
    var listComponentType = context.findType(type);
    var listProperty = context.findProperty(target, setter, listClass);
    var next = new SelectAtElement<List<X>, X>(new SelectListElement<>(leaf, listProperty), listComponentType, 0);
    return new MonoLens<>(context, type, next);
  }

  @Override
  public <X> PolyLens<I, X> selectAll(Setter<O, List<X>> setter, Class<X> type) {
    O target = LensUtils.newTracer(outputType);
    Class<List<X>> listClass = (Class<List<X>>)(Class<?>)List.class;
    Property<O, List<X>> listProperty = context.findProperty(target, setter, listClass);
    SelectAllElement<List<X>, X> next = new SelectAllElement<>(new SelectListElement<>(leaf, listProperty));
    return new PolyLens<>(context, type, next);
  }

  private static <I, O> LensConstructor<I, O, MonoLens<I, O>> constructor() {
    return MonoLens::new;
  }
}
