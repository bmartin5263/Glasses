package dev.bdon.lens;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Type {
  private Class<?> clazz;
  private Map<Class<?>, List<Property<Object, Object>>> properties;

  public Type(Class<?> clazz, List<Property<Object, Object>> properties) {
    this.clazz = clazz;
    this.properties = properties.stream()
        .collect(Collectors.groupingBy(Property::type));
  }

  public Map<Class<?>, List<Property<Object, Object>>> properties() {
    return properties;
  }

  public static Type from(Class<?> clazz) {
    var properties = ReflectionUtils.getFields(clazz).stream()
        .map(Property::from)
        .toList();
    return new Type(clazz, properties);
  }

  public <O, X> Property<O, X> findProperty(O target, Setter<O, X> setter, Class<X> propertyType) {
    var tracer = LensUtils.newTracer(propertyType);
    var possible = properties.get(propertyType).stream()
        .map(p -> new PossibleProperty<>((Property<O, X>) p, (X) p.get(target)))
        .toList();

    setter.set(target, tracer);

    for (var property : possible) {
      var value = property.get(target);
      property.resetOriginalValue(target);
      if (value == tracer) {
        return unchecked(property.actual());
      }
    }

    throw new LensInternalException("Unable to find property of type %s on target type %s", propertyType, target.getClass());
  }

  @SuppressWarnings("unchecked")
  public static <A, B, C, D> Property<A, B> unchecked(Property<C, D> property) {
    return (Property<A, B>) property;
  }

  private record PossibleProperty<I, O>(Property<I, O> actual, O originalValue) {
    public Object get(I target) {
      return actual.get(target);
    }

    public void resetOriginalValue(I target) {
      actual.set(target, originalValue);
    }
  }
}
