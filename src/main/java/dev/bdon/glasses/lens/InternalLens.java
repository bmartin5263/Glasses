package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;
import dev.bdon.glasses.path.Path;
import dev.bdon.glasses.type.IProperty;

import java.util.stream.Stream;

record InternalLens<O>(
    LensContext context,
    Element<?, O> leaf,
    Class<O> outputType
) {
  public Stream<IProperty<Object, Object>> properties(Path path) {
    var stack = path.dynamicNodeStack();
    return LensUtils.reverseElementChain(leaf)
        .filter(Element::isSelectionElement)
        .map(Element::asSelectionElement)
        .map(e -> e.property(stack))
        .map(IProperty::unchecked);
  }
}
