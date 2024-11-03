package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;
import dev.bdon.glasses.type.IProperty;

import java.util.Optional;
import java.util.stream.Stream;

record InternalLens<O>(
    LensContext context,
    Element<?, O> leaf,
    Class<O> outputType
) {
  public Stream<IProperty<Object, Object>> properties(Route route) {
    var stack = route.stack();
    return LensUtils.reverseElementChain(leaf)
        .map(e -> e.property(stack))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(p -> (IProperty<Object, Object>) p);
  }
}
