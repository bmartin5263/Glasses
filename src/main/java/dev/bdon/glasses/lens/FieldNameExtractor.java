package dev.bdon.glasses.lens;

import dev.bdon.glasses.type.FieldProperty;

import java.util.function.Function;

public interface FieldNameExtractor extends Function<FieldProperty<?, ?>, String> {
}
