package dev.bdon.glasses.type;

import dev.bdon.glasses.util.LensInternalException;
import dev.bdon.glasses.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FieldProperty<I, O> implements Property<I, O> {
  private final JavaField javaField;
  private final List<Type> genericArguments;

  public FieldProperty(JavaField javaField) {
    this(javaField, List.of());
  }

  public FieldProperty(JavaField javaField, List<Type> genericArguments) {
    this.javaField = javaField;
    this.genericArguments = genericArguments;
  }

  public String name() {
    return javaField.name();
  }

  @SuppressWarnings("unchecked")
  public <A extends Annotation> Optional<A> getAnnotation(Class<? extends A> clazz) {
    return javaField.annotations().stream()
        .filter(a -> a.annotationType() == clazz)
        .map(a -> (A) a)
        .findFirst();
  }

  private List<Type> genericArguments() {
    return genericArguments;
  }

  @Override
  public O get(I target) {
    return javaField.get(target);
  }

  @Override
  public void set(I target, O newValue) {
    javaField.set(target, newValue);
  }

  @Override
  public Class<O> type() {
    return javaField.type();
  }

  private boolean isCollection() {
    return ReflectionUtils.isCollection(type());
  }

  @SuppressWarnings("unchecked")
  public static <A, B, C, D> FieldProperty<A, B> unchecked(FieldProperty<C, D> property) {
    return (FieldProperty<A, B>) property;
  }

  public static <I, O> FieldProperty<I, O> from(JavaField field) {
    var genericType = field.actual().getGenericType();
    if (genericType instanceof ParameterizedType parameterizedType) {
      return new FieldProperty<>(field, List.of(parameterizedType.getActualTypeArguments()));
    }
    else if (genericType instanceof Class<?>) {
      return new FieldProperty<>(field);
    }
    else {
      throw new LensInternalException("Generic fields not supported: %s", field);
    }
  }

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

  @Override
  public String toString() {
    return "FieldProperty[" +
        "javaField=" + javaField +
        ", genericArguments=" + genericArguments +
        ']';
  }
}
