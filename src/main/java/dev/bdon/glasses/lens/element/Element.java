package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.DynamicComponent;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.type.IProperty;

import java.util.Deque;
import java.util.Optional;

public abstract class Element<I, O> {
  private final Element<Object, Object> parent;

  public Element() {
    this(null);
  }

  public Element(Element<?, ?> parent) {
    this.parent = unchecked(parent);
  }

  public <X, Y> Element<X, Y> parent() {
    return unchecked(parent);
  }

  public abstract Blurs<O> apply(LensRuntime runtime, Blur<I> blur);

  public abstract String pathComponent();

  public abstract Optional<IProperty<I, O>> property(Deque<DynamicComponent> components);

  @SuppressWarnings("unchecked")
  public static <A, B, C, D> Element<A, B> unchecked(Element<C, D> element) {
    return (Element<A, B>) element;
  }
}
