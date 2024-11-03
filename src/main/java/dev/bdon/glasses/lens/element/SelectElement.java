package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.util.Assert;
import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.type.Property;

public class SelectElement<I, O> extends Element<I, O> {
  private final Property<I, O> property;

  public SelectElement(
      Element<?, ?> parent,
      Property<I, O> property
  ) {
    super(parent);
    this.property = Assert.nonNullArgument(property, "property");
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
