package dev.bdon.glasses.lens;

import dev.bdon.glasses.type.Property;

import java.util.function.Function;

public interface FieldPropertyComponentExtractor extends Function<Property<?, ?>, String> {
}
