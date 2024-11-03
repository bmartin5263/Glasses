package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;

interface LensConstructor<I, O, L extends Lens<I, O>> {
  <X> L construct(LensContext context, Class<O> outputType, Element<X, O> leaf);
}
