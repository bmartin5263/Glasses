package dev.bdon.lens;

import java.util.List;

public class SelectListElement<I, O> extends Element<I, List<O>> {
  private final Property<I, List<O>> property;

  public SelectListElement(Element<?, ?> parent, Property<I, List<O>> property) {
    super(parent);
    this.property = Assert.argumentNonNull(property, "property");
  }

  @Override
  public Blurs<List<O>> apply(LensRuntime runtime, Blur<I> blur) {
    return Blurs.of(blur.next(property));
  }

  @Override
  public String pathComponent() {
    return "." + property.name();
  }
}
