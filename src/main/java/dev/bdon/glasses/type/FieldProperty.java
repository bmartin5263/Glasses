package dev.bdon.glasses.type;

import dev.bdon.glasses.lens.LensInternalException;
import dev.bdon.glasses.util.Default;
import dev.bdon.glasses.util.Getter;
import dev.bdon.glasses.util.ReflectionUtils;
import dev.bdon.glasses.util.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FieldProperty<I, O> implements Property<I, O> {
  private final String name;
  private final List<Annotation> annotations;
  private final Getter<I, O> getter;
  private final Setter<I, O> setter;
  private final Class<O> type;
  private final List<Type> genericArguments;

  public FieldProperty(String name, List<Annotation> annotations, Getter<I, O> getter, Setter<I, O> setter, Class<O> type) {
    this(name, annotations, getter, setter, type, List.of());
  }

  public FieldProperty(String name, List<Annotation> annotations, Getter<I, O> getter, Setter<I, O> setter, Class<O> type, List<Type> genericArguments) {
    this.name = name;
    this.annotations = Default.toEmpty(annotations);
    this.getter = getter;
    this.setter = setter;
    this.type = type;
    this.genericArguments = genericArguments;
  }

  public String name() {
    return name;
  }

  @SuppressWarnings("unchecked")
  public <A extends Annotation> Optional<A> getAnnotation(Class<? extends A> clazz) {
    return annotations.stream()
        .filter(a -> a.annotationType() == clazz)
        .map(a -> (A) a)
        .findFirst();
  }

  private List<Type> genericArguments() {
    return genericArguments;
  }

  @Override
  public O get(I target) {
    return getter.get(target);
  }

  @Override
  public void set(I target, O newValue) {
    setter.set(target, newValue);
  }

  @Override
  public Class<O> type() {
    return type;
  }

  private boolean isCollection() {
    return ReflectionUtils.isCollection(type);
  }

  @SuppressWarnings("unchecked")
  public static <A, B, C, D> FieldProperty<A, B> unchecked(FieldProperty<C, D> property) {
    return (FieldProperty<A, B>) property;
  }

  public static <I, O> FieldProperty<I, O> from(JavaField field) {
    var genericType = field.actual().getGenericType();
    if (genericType instanceof ParameterizedType parameterizedType) {
      return new FieldProperty<>(field.name(), field.annotations(), field::get, field::set, field.type(), List.of(parameterizedType.getActualTypeArguments()));
    }
    else if (genericType instanceof Class<?>) {
      return new FieldProperty<>(field.name(), field.annotations(), field::get, field::set, field.type());
    }
    else {
      throw new LensInternalException("Unhandled generic supertype %s", genericType);
    }
  }

  @Override
  public Stream<Class<?>> getClasses() {
    var result = Stream.<Class<?>>builder();
    if (isCollection()) {
      for (var genericType : genericArguments()) {
        result.add((Class<?>) genericType);
      }
    }
    else {
      result.add(type());
    }
    return result.build();
  }
}
