package dev.bdon.glasses.type;

import dev.bdon.glasses.util.ReflectionUtils;
import dev.bdon.glasses.lens.LensInternalException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeRegistry {
  private final Map<Class<?>, Type<?>> TYPE_BY_CLASS = new ConcurrentHashMap<>();

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
      registerChildren(type);
    }
  }

  private void registerChildren(Type<?> type) {
    type.properties().forEach(property -> {
      if (property.isCollection()) {
        for (var genericType : property.genericArguments()) {
          register((Class<?>) genericType);
        }
      }
      else {
        register(property.type());
      }
    });
  }

  public <T> Type<T> getType(T object) {
    var type = TYPE_BY_CLASS.get(object.getClass());
    if (type == null) {
      throw new LensInternalException("'%s' does not have a Type registered".formatted(object));
    }
    return Type.unchecked(type);
  }

  public <T> Type<T> getType(Class<T> clazz) {
    var type = TYPE_BY_CLASS.get(clazz);
    if (type == null) {
      throw new LensInternalException("'%s' does not have a Type registered".formatted(clazz));
    }
    return Type.unchecked(type);
  }

  private static class BooleanHolder {
    public boolean value;
  }
}
