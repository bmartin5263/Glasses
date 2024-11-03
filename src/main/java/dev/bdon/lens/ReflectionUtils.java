package dev.bdon.lens;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

public class ReflectionUtils {
  private static final Map<Class<?>, List<JavaField>> FIELD_CACHE = new HashMap<>();
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

  public static List<JavaField> getFields(Class<?> clazz) {
    return FIELD_CACHE.computeIfAbsent(clazz, currentClass -> {
      var result = new ArrayList<JavaField>();
      while (currentClass != null && !isPrimitive(currentClass)) {
        for (var field : currentClass.getDeclaredFields()) {
          result.add(new JavaField(field));
        }
        currentClass = currentClass.getSuperclass();
      }
      return List.copyOf(result);
    });
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
