package dev.bdon.glasses.type;

import dev.bdon.glasses.lens.LensInternalException;
import dev.bdon.glasses.lens.LensUtils;
import dev.bdon.glasses.util.ReflectionUtils;
import dev.bdon.glasses.util.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Type<T> {
  private final Class<T> clazz;
  private final Map<Class<?>, List<Property<T, Object>>> properties;

  public Type(Class<T> clazz, List<Property<T, Object>> properties) {
    this.clazz = clazz;
    this.properties = properties.stream()
        .collect(Collectors.groupingBy(Property::type));
  }

  public Stream<Property<T, Object>> properties() {
    return properties.values().stream().flatMap(List::stream);
  }

  public static <T> Type<T> from(Class<T> clazz) {
    var properties = ReflectionUtils.getFields(clazz).stream()
        .map(Property::<T, Object>from)
        .toList();
    return new Type<>(clazz, properties);
  }

  public <X> Property<T, X> findProperty(T target, Setter<T, X> setter, Class<X> propertyType) {
    var tracer = LensUtils.newTracer(propertyType);
    var possible = properties.get(propertyType).stream()
        .map(p -> new PossibleProperty<>(Property.unchecked(p), (X) p.get(target)))
        .toList();

    setter.set(target, tracer);

    for (var property : possible) {
      var value = property.get(target);
      property.resetOriginalValue(target);
      if (value == tracer) {
        return Property.unchecked(property.actual());
      }
    }

    throw new LensInternalException("Unable to find property of type %s on target type %s", propertyType, target.getClass());
  }

  public T instantiate() {
    return LensUtils.newTracer(clazz);
  }

  @SuppressWarnings("unchecked")
  public static <A, B> Type<A> unchecked(Type<B> property) {
    return (Type<A>) property;
  }

  public Class<T> javaClass() {
    return clazz;
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
