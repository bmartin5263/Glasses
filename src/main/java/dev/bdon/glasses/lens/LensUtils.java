package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LensUtils {
  private static final Map<Class<?>, Supplier<?>> TRACER_FACTORY_BY_TYPE = new ConcurrentHashMap<>();

  private LensUtils() {}

  @SuppressWarnings("unchecked")
  public static <T> T newTracer(Class<T> type) {
    return (T) TRACER_FACTORY_BY_TYPE.computeIfAbsent(type, k -> instantiateTracerFactory((Class<Object>) k)).get();
  }

  // Reverses the element linked list
  static <A, B> Stream<Element<A, B>> reverseElementChain(Element<A, B> last) {
    var elements = new ArrayList<Element<A, B>>();
    for (var current = last; current != null; current = current.parent()) {
      elements.add(current);
    }
    Collections.reverse(elements);
    return elements.stream();
  }

  @SuppressWarnings("unchecked")
  private static <T> Supplier<T> instantiateTracerFactory(Class<T> type) {
    if (List.class.isAssignableFrom(type)) {
      return () -> (T) new ArrayList<>();
    }

    Constructor<T> constructor;
    try {
      constructor = type.getDeclaredConstructor();
      constructor.setAccessible(true);
    }
    catch (NoSuchMethodException e) {
      throw new LensConfigurationException(e, "Class '%s' does not have a default constructor available", type);
    }
    return () -> {
      try {
        return constructor.newInstance();
      } catch (InvocationTargetException e) {
        throw new LensConfigurationException(e.getCause(), "Class '%s' default constructor threw an exception", type);
      } catch (InstantiationException e) {
        throw new LensConfigurationException(e, "Class '%s' could not be instantiated", type);
      } catch (IllegalAccessException e) {
        throw new LensConfigurationException(e, "Constructor on class '%s' could not be made accessible", type);
      }
    };
  }

}
