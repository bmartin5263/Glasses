package dev.bdon.lens;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class ReflectionUtils {
  private static final Map<Class<?>, List<Field>> FIELD_CACHE = new HashMap<>();

  public static List<Field> getFields(Class<?> clazz) {
    return FIELD_CACHE.computeIfAbsent(clazz, currentClass -> {
      var result = new ArrayList<Field>();
      while (currentClass != null && currentClass != Object.class) {
        for (var field : currentClass.getDeclaredFields()) {
          field.setAccessible(true);
          result.add(field);
        }
        currentClass = currentClass.getSuperclass();
      }
      return List.copyOf(result);
    });
  }

  public static Field findFieldWithValueOn(Object target, Object value) {
    Field found = null;
    var fields = getFields(target.getClass());
    for (var field : fields) {
      try {
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
      } catch (IllegalAccessException e) {
        e.printStackTrace();
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

}
