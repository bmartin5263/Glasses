package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.util.Assert;
import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.type.Property;

import java.util.List;

public class SelectListElement<I, O> extends Element<I, List<O>> {
  private final Property<I, List<O>> property;

  public SelectListElement(Element<?, ?> parent, Property<I, List<O>> property) {
    super(parent);
    this.property = Assert.nonNullArgument(property, "property");
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
