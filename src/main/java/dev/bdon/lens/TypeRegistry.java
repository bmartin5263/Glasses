package dev.bdon.lens;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeRegistry {
  private final Map<Class<?>, Type> TYPE_BY_CLASS = new ConcurrentHashMap<>();

  public void register(Class<?> clazz) {
    if (ReflectionUtils.isPrimitive(clazz)) {
      return;
    }
    var didCompute = new BooleanHolder();
    var type = TYPE_BY_CLASS.computeIfAbsent(clazz, k -> {
      didCompute.value = true;
      return Type.from(k);
    });
    if (didCompute.value) {
      for (var property : type.properties().values().stream().flatMap(List::stream).toList()) {
        if (property.isCollection()) {
          for (var genericType : property.genericArguments()) {
            register((Class<?>) genericType);
          }
        }
        else {
          register(property.type());
        }
      }
    }
  }

  public Type getType(Class<?> clazz) {
    var type = TYPE_BY_CLASS.get(clazz);
    if (type == null) {
      throw new LensInternalException("%s does not have a Type registered".formatted(clazz));
    }
    return type;
  }

  private static class BooleanHolder {
    public boolean value;
  }
}
