package dev.bdon.lens;

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
}
