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
    return Blurs.single(blur.next(property));
  }

  @Override
  public O overwrite(I input, O newValue) {
    var previousValue = property.get(input);
    property.set(input, newValue);
    return previousValue;
  }

  @Override
  public O overwriteWithDefaultIfNull(I input) {
    var currentValue = property.get(input);
    if (currentValue == null) {
      currentValue = LensUtils.newTracer(property.type());
      property.set(input, currentValue);
    }
    return currentValue;
  }

  @Override
  public String pathComponent() {
    return "." + property.name();
  }
}
