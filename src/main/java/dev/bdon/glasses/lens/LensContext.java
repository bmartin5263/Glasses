package dev.bdon.glasses.lens;

import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.type.TypeRegistry;
import dev.bdon.glasses.util.Setter;

public class LensContext {
  public static LensContext DEFAULT = new LensContext();
  private final TypeRegistry typeRegistry = new TypeRegistry();

  public <T> MonoLens<T, T> create(Class<T> type) {
    typeRegistry.register(type);
    return new MonoLens<>(this, type);
  }

  public <O, X> Property<O, X> findProperty(O target, Setter<O, X> setter, Class<X> propertyType) {
    var type = typeRegistry.getType(target);
    return type.findProperty(target, setter, propertyType);
  }

  public <T> Type<T> findType(Class<T> type) {
    return typeRegistry.getType(type);
  }
}
