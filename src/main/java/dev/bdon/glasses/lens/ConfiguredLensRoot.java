package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.PathlessElement;

public class ConfiguredLensRoot {
  private final LensContext context;
  private final PathlessElement<?, ?> configurationElement;

  public ConfiguredLensRoot(LensContext context, PathlessElement<?, ?> configurationElement) {
    this.context = context;
    this.configurationElement = configurationElement;
  }

  public PathlessElement<?, ?> configurationElement() {
    return configurationElement;
  }

  public <T> MonoLens<T, T> create(Class<T> type) {
    return context.create(type);
  }
}
