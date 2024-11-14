package dev.bdon.glasses.lens;

public class LensConfigurationBuilder {
  private FieldNameExtractor fieldNameExtractor;

  public LensConfigurationBuilder fieldNameExtractor(FieldNameExtractor fieldNameExtractor) {
    this.fieldNameExtractor = fieldNameExtractor;
    return this;
  }

  public LensConfiguration build() {
    return new LensConfiguration(fieldNameExtractor);
  }
}
