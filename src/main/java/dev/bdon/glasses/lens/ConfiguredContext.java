package dev.bdon.glasses.lens;

public class ConfiguredContext {
  private final LensContext context;
  private final LensConfiguration configuration;

  public ConfiguredContext(LensContext context, LensConfiguration configuration) {
    this.context = context;
    this.configuration = configuration;
  }

  public <T> MonoLens<T, T> create(Class<T> type) {
    return context.createLens(type, configuration);
  }
}
