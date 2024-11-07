package dev.bdon.glasses.path;

import java.lang.reflect.Field;

public interface FieldNameExtractor {
  String getName(Field field);
}
