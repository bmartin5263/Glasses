package dev.bdon.glasses.type;

import dev.bdon.glasses.util.LensConfigurationException;
import dev.bdon.glasses.util.LensInternalException;
import dev.bdon.glasses.util.ReflectionUtils;
import dev.bdon.glasses.util.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Type<T> {
  private final Class<T> clazz;
  private final Map<Class<?>, List<FieldProperty<T, Object>>> propertiesByType;

  public Type(Class<T> clazz, List<FieldProperty<T, Object>> properties) {
    this.clazz = clazz;
    this.propertiesByType = properties.stream()
        .collect(Collectors.groupingBy(FieldProperty::type));
  }

  public Stream<FieldProperty<T, Object>> properties() {
    return propertiesByType.values().stream().flatMap(List::stream);
  }

  public static <T> Type<T> from(Class<T> clazz) {
    var properties = ReflectionUtils.getFields(clazz).stream()
        .map(FieldProperty::<T, Object>from)
        .toList();
    return new Type<>(clazz, properties);
  }

  @SuppressWarnings("unchecked")
  public <X> FieldProperty<T, X> findProperty(T target, Setter<T, X> setter, Class<X> propertyType) {
    // Create a tracer that will be injected into the target object and then located using reflection
    var tracer = Tracers.newTracer(propertyType);

    // Limit our search space by the type of property we are finding
    var possibleProperties = propertiesByType.getOrDefault(propertyType, List.of())
        .stream()
        .map(p -> new PossibleProperty<>(FieldProperty.unchecked(p), (X) p.get(target)))
        .toList();

    // Inject the tracer
    setter.set(target, tracer);

    // Find the property that represents the field the tracer got injected into
    FieldProperty<T, X> result = null;
    for (var property : possibleProperties) {
      var value = property.get(target);
      if (value == tracer) {
        if (result != null) {
          // Fail fast instead of potentially returning the wrong property
          throw new LensInternalException("Ambiguous property due to non-unique tracer. %s and %s match for type %s", property.actual(), result, propertyType);
        }

        // Reset the object back to its original pre-tracer-injection state
        property.resetOriginalValue(target);

        // Keep track of the result, but keep scanning to ensure there is no ambiguity
        result = FieldProperty.unchecked(property.actual());
      }
    }

    if (result != null) {
      return result;
    }

    throw new LensConfigurationException("Unable to find property of type %s on target type %s", propertyType, target.getClass());
  }

  @SuppressWarnings("unchecked")
  public static <A, B> Type<A> unchecked(Type<B> property) {
    return (Type<A>) property;
  }

  public Class<T> javaClass() {
    return clazz;
  }

  private record PossibleProperty<I, O>(FieldProperty<I, O> actual, O originalValue) {
    public Object get(I target) {
      return actual.get(target);
    }

    public void resetOriginalValue(I target) {
      actual.set(target, originalValue);
    }
  }
}
