package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.DynamicComponent;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.type.IProperty;
import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.util.Assert;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

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

  @Override
  public Optional<IProperty<I, List<O>>> property(Deque<DynamicComponent> components) {
    return Optional.of(property);
  }
}
