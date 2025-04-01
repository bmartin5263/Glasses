package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.ConfigurationElement;
import dev.bdon.glasses.type.FieldProperty;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.type.TypeRegistry;
import dev.bdon.glasses.util.Setter;

import java.util.function.Consumer;

public class LensContext {
  public static LensContext DEFAULT = new LensContext(LensConfiguration.DEFAULT);

  private final TypeRegistry typeRegistry = new TypeRegistry();
  private final LensConfiguration defaultLensConfiguration;

  public LensContext(LensConfiguration defaultLensConfiguration) {
    this.defaultLensConfiguration = defaultLensConfiguration;
  }

  public <T> MonoLens<T, T> createLens(Class<T> type) {
    typeRegistry.register(type);
    return new MonoLens<>(this, type);
  }

  public <T> MonoLens<T, T> createLens(Class<T> type, LensConfiguration configuration) {
    typeRegistry.register(type);
    return new MonoLens<>(this, type, new ConfigurationElement<>(configuration));
  }

  public ConfiguredContext configureLensRoot(Consumer<LensConfigurationBuilder> configurer) {
    var builder = new LensConfigurationBuilder();
    configurer.accept(builder);
    return new ConfiguredContext(this, builder.build());
  }

  <O, X> FieldProperty<O, X> findProperty(O target, Setter<O, X> setter, Class<X> propertyType) {
    var type = typeRegistry.getType(target);
    return type.findProperty(target, setter, propertyType);
  }

  <T> Type<T> findType(Class<T> type) {
    return typeRegistry.getType(type);
  }

  LensRuntime createRuntime(Lens<?, ?> lens) {
    var runtime = new LensRuntime(lens);
    defaultLensConfiguration.accept(runtime);
    runtime.validate();
    return runtime;
  }
}
