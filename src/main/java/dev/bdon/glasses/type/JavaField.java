package dev.bdon.glasses.type;

import dev.bdon.glasses.lens.LensInternalException;
import dev.bdon.glasses.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public record JavaField(Field actual) {

  public JavaField(Field actual) {
    this.actual = Assert.nonNullArgument(actual, "actual");
    actual.setAccessible(true);
  }

  public String name() {
    return actual.getName();
  }

  public List<Annotation> annotations() {
    return List.of(actual.getAnnotations());
  }

  @SuppressWarnings("unchecked")
  public <O> Class<O> type() {
    return (Class<O>) actual.getType();
  }

  @SuppressWarnings("unchecked")
  public <I, O> O get(I target) {
    try {
      return (O) actual.get(target);
    } catch (IllegalAccessException e) {
      throw new LensInternalException("Unable to read field %s of %s", actual, actual.getDeclaringClass());
    }
  }

  public <I, O> void set(I target, O newValue) {
    try {
      actual.set(target, newValue);
    } catch (IllegalAccessException e) {
      throw new LensInternalException("Unable to write field %s of %s", actual, actual.getDeclaringClass());
    }
  }
}
