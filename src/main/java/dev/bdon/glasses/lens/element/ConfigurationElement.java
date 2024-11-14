package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensConfiguration;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.path.Path;

public class ConfigurationElement<I> extends PathlessElement<I, I> {
  private final LensConfiguration lensConfiguration;

  public ConfigurationElement(LensConfiguration lensConfiguration) {
    this.lensConfiguration = lensConfiguration;
  }

  public ConfigurationElement(Element<?, ?> parent, LensConfiguration lensConfiguration) {
    super(parent);
    this.lensConfiguration = lensConfiguration;
  }

  @Override
  public Blurs<I> apply(LensRuntime runtime, Blur<I> blur) {
    lensConfiguration.accept(runtime);
    return Blurs.of(blur);
  }

  @Override
  public void buildPath(LensRuntime runtime, Path path) {
    lensConfiguration.accept(runtime);
  }
}
