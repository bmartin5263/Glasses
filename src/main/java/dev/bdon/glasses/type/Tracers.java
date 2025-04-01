package dev.bdon.glasses.type;

import dev.bdon.glasses.util.LensConfigurationException;
import dev.bdon.glasses.util.LensConventionViolatedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Tracers are objects that are simply unique default-constructed instances of arbitrary types that are used
 * to "trace" connections between objects and their children.
 *
 * Tracers may be used to determine which field of a class a Setter function is referring to, or could be used
 * to default construct a object-tree when overriding a previously non-existent value.
 */
public class Tracers {
  private static final Map<Class<?>, Supplier<?>> TRACER_FACTORY_BY_TYPE = new ConcurrentHashMap<>();

  /**
   * For these types, there is no default constructor available and many are boxed types for primitives.
   * We need to suppress a lot of warnings for this because this is a very unconventional use-case that
   */
  @SuppressWarnings({"removal", "UnnecessaryBoxing", "CachedNumberConstructorCall", "BooleanConstructorCall"})
  private static final Map<Class<?>, Supplier<?>> NON_DEFAULT_CONSTRUCTABLE_TYPES = Map.of(
      Boolean.class, () -> new Boolean(false),
      Byte.class, () -> new Byte((byte) 0xC8),
      Character.class, () -> new Character((char) 0xC8C8),
      Short.class, () -> new Short((short) 0xC8C8),
      Integer.class, () -> new Integer(0xC8C8C8C8),
      Long.class, () -> new Long(0xC8C8C8C8C8C8C8C8L),
      Float.class, () -> new Float(0xC8C8C8C8),
      Double.class, () -> new Double(0xC8C8C8C8C8C8C8C8L),
      BigDecimal.class, () -> new BigDecimal("1.0"),
      BigInteger.class, () -> new BigInteger("1")
  );

  /**
   * Generate a new tracer instance of the specified type. This tracer is guaranteed to be unique across all
   * instances of T
   *
   * @param type Type of tracer
   * @param <T> Type of tracer
   * @return a new tracer
   */
  @SuppressWarnings("unchecked")
  public static <T> T newTracer(Class<T> type) {
    return (T) TRACER_FACTORY_BY_TYPE.computeIfAbsent(type, k -> instantiateTracerFactory((Class<Object>) k)).get();
  }

  @SuppressWarnings("unchecked")
  private static <T> Supplier<T> instantiateTracerFactory(Class<T> type) {
    if (type.isPrimitive()) {
      throw new LensConventionViolatedException("Primitive types are not supported: %s", type);
    }

    var factoryForNonDefaultConstructableType = NON_DEFAULT_CONSTRUCTABLE_TYPES.get(type);
    if (factoryForNonDefaultConstructableType != null) {
      return (Supplier<T>) factoryForNonDefaultConstructableType;
    }

    if (List.class.isAssignableFrom(type)) {
      // TODO - special case needed?
      return () -> (T) new ArrayList<>();
    }

    Constructor<T> constructor;
    try {
      constructor = type.getDeclaredConstructor();
      constructor.setAccessible(true);
    }
    catch (NoSuchMethodException e) {
      throw new LensConventionViolatedException(e, "Class '%s' does not have a default constructor available", type);
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
