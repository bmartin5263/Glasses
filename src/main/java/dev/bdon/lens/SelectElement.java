package dev.bdon.lens;

public class SelectElement<I, O> extends Element<I, O> {
  private final Property<I, O> property;

  public SelectElement(
      Element<?, ?> parent,
      Property<I, O> property
  ) {
    super(parent);
    this.property = Assert.argumentNonNull(property, "property");
  }

  @Override
  public Blurs<O> apply(LensRuntime runtime, Blur<I> blur) {
    return Blurs.of(blur.next(property));
  }

  @Override
  public String pathComponent() {
    return "." + property.name();
  }
}
