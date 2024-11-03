package dev.bdon.lens;

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

  @SuppressWarnings("unchecked")
  public static <A, B, C, D> Element<A, B> unchecked(Element<C, D> element) {
    return (Element<A, B>) element;
  }
}
