package dev.bdon.glasses.lens;

import dev.bdon.glasses.type.FieldProperty;
import dev.bdon.glasses.util.Assert;
import dev.bdon.glasses.util.LensConfigurationException;

public class LensRuntime {
  private final Lens<?, ?> lens;
  private FieldNameExtractor fieldPathExtractor;

  public LensRuntime(Lens<?, ?> lens) {
    this.lens = lens;
  }

  public LensContext context() {
    return lens.context();
  }

  public Lens<?, ?> lens() {
    return lens;
  }

  public void validate() {
    Assert.nonNull(fieldPathExtractor, LensConfigurationException::new, "No fieldPathExtractor defined");
  }

  public void setNamePathExtractor(FieldNameExtractor fieldPathExtractor) {
    this.fieldPathExtractor = fieldPathExtractor;
  }

  public <I, O> String extractFieldPath(FieldProperty<I, O> property) {
    try {
      return fieldPathExtractor.apply(property);
    }
    catch (Exception e) {
      // TODO - what should we do?
      return property.name();
    }
  }
}
