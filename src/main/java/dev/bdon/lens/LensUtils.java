package dev.bdon.lens;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class LensUtils {
  private static final Map<Class<?>, Supplier<?>> TRACER_FACTORY_BY_TYPE = new ConcurrentHashMap<>();

  private LensUtils() {}

  @SuppressWarnings("unchecked")
  static <T> T getTracer(Class<T> type) {
    return (T) TRACER_FACTORY_BY_TYPE.computeIfAbsent(type, k -> instantiateTracerFactory((Class<Object>) k)).get();
  }

  // Reverses the element linked list
  static Iterable<Element<Object, Object>> gatherElements(Element<Object, Object> last) {
    var elements = new ArrayList<Element<Object, Object>>();
    for (Element<Object, Object> current = last; current != null; current = current.getParent()) {
      elements.add(current);
    }
    Collections.reverse(elements);
    return elements;
  }

  private static <T> Supplier<T> instantiateTracerFactory(Class<T> type) {
    return () -> {
      try {
        return type.getDeclaredConstructor().newInstance();
      }
      catch (NoSuchMethodException e) {
        throw new LensConfigurationException(e, "Class '%s' does not have a default constructor available", type);
      } catch (InvocationTargetException e) {
        throw new LensConfigurationException(e.getCause(), "Class '%s' default constructor threw an exception", type);
      } catch (InstantiationException e) {
        throw new LensConfigurationException(e, "Class '%s' could not be instantiated", type);
      } catch (IllegalAccessException e) {
        throw new LensConfigurationException(e, "Class '%s' does not have a publicly available constructor", type);
      }
    };
  }

}
