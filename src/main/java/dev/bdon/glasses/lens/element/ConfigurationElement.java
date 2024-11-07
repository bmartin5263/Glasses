package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensConfigurer;
import dev.bdon.glasses.lens.LensRuntime;

public class ConfigurationElement<I> extends PathlessElement<I, I> {
  private final LensConfigurer lensConfigurer;

  public ConfigurationElement(LensConfigurer lensConfigurer) {
    this.lensConfigurer = lensConfigurer;
  }

  public ConfigurationElement(Element<?, ?> parent, LensConfigurer lensConfigurer) {
    super(parent);
    this.lensConfigurer = lensConfigurer;
  }

  @Override
  public Blurs<I> apply(LensRuntime runtime, Blur<I> blur) {
    lensConfigurer.accept(runtime);
    return Blurs.of(blur);
  }

}
