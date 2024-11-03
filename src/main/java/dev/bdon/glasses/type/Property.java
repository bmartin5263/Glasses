package dev.bdon.glasses.type;

import dev.bdon.glasses.util.Getter;
import dev.bdon.glasses.util.ReflectionUtils;
import dev.bdon.glasses.util.Setter;
import dev.bdon.glasses.lens.LensInternalException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class Property<I, O> {
  private final String name;
  private final Getter<I, O> getter;
  private final Setter<I, O> setter;
  private final Class<O> type;
  private final List<Type> genericArguments;

  public Property(String name, Getter<I, O> getter, Setter<I, O> setter, Class<O> type) {
    this(name, getter, setter, type, List.of());
  }

  public Property(String name, Getter<I, O> getter, Setter<I, O> setter, Class<O> type, List<Type> genericArguments) {
    this.name = name;
    this.getter = getter;
    this.setter = setter;
    this.type = type;
    this.genericArguments = genericArguments;
  }

  public String name() {
    return name;
  }

  public List<Type> genericArguments() {
    return genericArguments;
  }

  public O get(I target) {
    return getter.get(target);
  }

  public void set(I target, O newValue) {
    setter.set(target, newValue);
  }

  public Class<O> type() {
    return type;
  }

  public boolean isCollection() {
    return ReflectionUtils.isCollection(type);
  }

  @SuppressWarnings("unchecked")
  public static <A, B, C, D> Property<A, B> unchecked(Property<C, D> property) {
    return (Property<A, B>) property;
  }

  public static <I, O> Property<I, O> from(JavaField field) {
    var genericType = field.actual().getGenericType();
    if (genericType instanceof ParameterizedType parameterizedType) {
      return new Property<>(field.name(), field::get, field::set, field.type(), List.of(parameterizedType.getActualTypeArguments()));
    }
    else if (genericType instanceof Class<?>) {
      return new Property<>(field.name(), field::get, field::set, field.type());
    }
    else {
      throw new LensInternalException("Unhandled generic supertype %s", genericType);
    }
  }
}
