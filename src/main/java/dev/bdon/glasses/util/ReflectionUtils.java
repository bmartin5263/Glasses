package dev.bdon.glasses.util;

import dev.bdon.glasses.type.JavaField;
import dev.bdon.glasses.lens.LensInternalException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReflectionUtils {
  private static final Set<Class<?>> PRIMITIVES = Set.of(
      Boolean.class,
      Byte.class,
      Character.class,
      Short.class,
      Integer.class,
      Long.class,
      Float.class,
      Double.class,
      Object.class,
      String.class,
      BigDecimal.class,
      BigInteger.class
  );
  private static final Map<Class<?>, List<JavaField>> FIELD_CACHE = new HashMap<>(PRIMITIVES
      .stream()
      .collect(Collectors.toMap(Function.identity(), k -> List.of()))
  );

  public static List<JavaField> getFields(Class<?> clazz) {
    return FIELD_CACHE.computeIfAbsent(clazz, currentClass -> {
      var result = new ArrayList<JavaField>();
      getFields(clazz, result);
      return List.copyOf(result);
    });
  }

  private static void getFields(Class<?> clazz, List<JavaField> fields) {
    if (clazz == null || isPrimitive(clazz)) {
      return;
    }
    getFields(clazz.getSuperclass(), fields);
    for (var field : clazz.getDeclaredFields()) {
      fields.add(new JavaField(field));
    }
  }

  public static JavaField findFieldWithValueOn(Object target, Object value) {
    JavaField found = null;
    var fields = getFields(target.getClass());
    for (var field : fields) {
      var fieldValue = field.get(target);
      if (fieldValue == value) {
        if (found != null) {
          throw new LensInternalException(
              "Found multiple field containing value %s on object type %s. %s : %s",
              value, target.getClass(), field, found
          );
        }
        found = field;
      }
    }
    if (found != null) {
      return found;
    }
    throw new LensInternalException(
        "Unable to find field containing value %s on object type %s",
        value, target.getClass()
    );
  }

  public static boolean isPrimitive(Class<?> clazz) {
    return clazz.isPrimitive() || PRIMITIVES.contains(clazz);
  }

  public static boolean isCollection(Class<?> clazz) {
    return Collection.class.isAssignableFrom(clazz);
  }
}
