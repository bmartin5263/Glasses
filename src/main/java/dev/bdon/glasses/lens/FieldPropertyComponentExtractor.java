package dev.bdon.glasses.lens;

import dev.bdon.glasses.type.FieldProperty;

import java.util.function.Function;

public interface FieldPropertyComponentExtractor extends Function<FieldProperty<?, ?>, String> {
}
