package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.ConfigurationElement;
import dev.bdon.glasses.lens.element.Element;
import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.type.TypeRegistry;
import dev.bdon.glasses.util.Setter;

import java.util.function.Consumer;

public class LensContext {
  public static LensContext DEFAULT = new LensContext(new LensConfigurer());

  private final TypeRegistry typeRegistry = new TypeRegistry();
  private final LensConfigurer defaultLensConfiguration;

  public LensContext(LensConfigurer defaultLensConfiguration) {
    this.defaultLensConfiguration = defaultLensConfiguration;
  }

  public <T> MonoLens<T, T> create(Class<T> type) {
    typeRegistry.register(type);
    return new MonoLens<>(this, type);
  }

  public <T> MonoLens<T, T> create(Class<T> type, ConfiguredLensRoot configuration) {
    typeRegistry.register(type);
    return new MonoLens<>(this, type, Element.unchecked(configuration.configurationElement()));
  }

  public <O, X> Property<O, X> findProperty(O target, Setter<O, X> setter, Class<X> propertyType) {
    var type = typeRegistry.getType(target);
    return type.findProperty(target, setter, propertyType);
  }

  public <T> Type<T> findType(Class<T> type) {
    return typeRegistry.getType(type);
  }

  public ConfiguredLensRoot configure(Consumer<LensConfigurerBuilder> configurer) {
    var builder = new LensConfigurerBuilder();
    configurer.accept(builder);
    return new ConfiguredLensRoot(this, new ConfigurationElement<>(builder.build()));
  }

  public LensConfigurer defaultLensConfiguration() {
    return defaultLensConfiguration;
  }
}
