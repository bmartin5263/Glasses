package dev.bdon.lens;

public abstract class Element<I, O> {
  private final Element<Object, Object> parent;

  public Element() {
    this(null);
  }

  public Element(Element<?, ?> parent) {
    this.parent = (Element<Object, Object>)parent;
  }

  @SuppressWarnings("unchecked")
  public <X, Y> Element<X, Y> getParent() {
    return (Element<X, Y>) parent;
  }

  public abstract Blurs<O> apply(Blur<I> blur);

  public abstract O overwrite(I input, O newValue);

  public abstract O overwriteWithDefaultIfNull(I input);

  public abstract String pathComponent();
}
