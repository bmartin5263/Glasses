package dev.bdon.glasses.lens;

import dev.bdon.glasses.type.FieldProperty;

import java.util.function.Consumer;

public class LensConfiguration implements Consumer<LensRuntime> {
  private static final FieldNameExtractor DEFAULT_FIELD_NAME_EXTRACTOR = FieldProperty::name;
  public static final LensConfiguration DEFAULT = new LensConfiguration(
      DEFAULT_FIELD_NAME_EXTRACTOR
  );

  private final FieldNameExtractor fieldNameExtractor;

  public LensConfiguration(FieldNameExtractor fieldNameExtractor) {
    this.fieldNameExtractor = fieldNameExtractor;
  }

  @Override
  public void accept(LensRuntime lensRuntime) {
    if (fieldNameExtractor != null) {
      lensRuntime.setNamePathExtractor(fieldNameExtractor);
    }
  }
}
