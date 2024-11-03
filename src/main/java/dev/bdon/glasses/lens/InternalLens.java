package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;

record InternalLens<O>(
    LensContext context,
    Element<?, O> leaf,
    Class<O> outputType
) {
}
