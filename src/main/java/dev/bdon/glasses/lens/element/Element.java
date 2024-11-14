package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.path.Path;

public abstract sealed class Element<I, O> permits PathlessElement, SelectionElement {
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

  public abstract void buildPath(LensRuntime runtime, Path path);

  public boolean isSelectionElement() {
    return this instanceof SelectionElement<I,O>;
  }

  public SelectionElement<I, O> asSelectionElement() {
    return (SelectionElement<I, O>) this;
  }

  public boolean isConfigurationElement() {
    return this instanceof PathlessElement<I,O>;
  }

  public PathlessElement<I, O> asConfigurationElement() {
    return (PathlessElement<I, O>) this;
  }

  @SuppressWarnings("unchecked")
  public static <A, B, C, D> Element<A, B> unchecked(Element<C, D> element) {
    return (Element<A, B>) element;
  }
}
