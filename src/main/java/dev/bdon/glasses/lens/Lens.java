package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;
import dev.bdon.glasses.lens.element.SelectionElement;
import dev.bdon.glasses.path.Path;
import dev.bdon.glasses.util.Setter;

import java.util.List;
import java.util.function.Consumer;

public sealed interface Lens<I, O> permits MonoLens, PolyLens {
  // Building Methods
  Lens<I, O> configure(Consumer<LensConfigurerBuilder> configurer);
  <X> Lens<I, X> select(Setter<O, X> setter, Class<X> type);
  <X> Lens<I, X> selectFirst(Setter<O, List<X>> setter, Class<X> type);
  <X> PolyLens<I, X> selectAll(Setter<O, List<X>> getter, Class<X> type);

  // Execution Methods
  List<Image<O>> focusToList(I target);
  void override(Image<O> image, O newValue);

  // Factory Methods
  static ConfiguredLensRoot configureRoot(Consumer<LensConfigurerBuilder> configurer) {
    return LensContext.DEFAULT.configure(configurer);
  }

  static <T> MonoLens<T, T> create(Class<T> type) {
    return LensContext.DEFAULT.create(type);
  }

  // Accessors
  LensContext context();
  <X> Element<X, O> leaf(); // TODO - hide from interface
  Class<O> outputType();

  default String path() {
    var leaf = leaf();
    var path = new Path(LensUtils.reverseElementChain(leaf)
        .filter(Element::isSelectionElement)
        .map(Element::asSelectionElement)
        .map(SelectionElement::pathNode)
        .toList());
    return path.toString();
  }

  @SuppressWarnings("unchecked")
  static <A, B, C, D> Lens<A, B> unchecked(Lens<C, D> lens) {
    return (Lens<A, B>) lens;
  }

  @SuppressWarnings("unchecked")
  static <O> List<Image<O>> reify(List<Image<Object>> images) {
    return (List<Image<O>>) (List<?>) images;
  }
}
