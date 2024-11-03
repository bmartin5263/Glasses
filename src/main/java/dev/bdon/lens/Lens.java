package dev.bdon.lens;

import java.util.List;

public sealed interface Lens<I, O> permits MonoLens, PolyLens {
  // Building Methods
  <X> Lens<I, X> select(Setter<O, X> setter, Class<X> type);
  <X, L extends List<X>> Lens<I, X> selectFirst(Getter<O, L> getter);
  <X> PolyLens<I, X> selectAll(Setter<O, List<X>> getter, Class<X> type);

  // Execution Methods
  List<Image<O>> focusToList(I target);

  // Factory Methods
  static <T> MonoLens<T, T> create(Class<T> type) {
    return LensContext.DEFAULT.create(type);
  }

  // Accessors
  LensContext context();
  <X> Element<X, O> leaf();

  default String path() {
    var leaf = leaf();
    var elements = LensUtils.gatherElements((Element<Object, Object>) leaf);
    var iter = elements.iterator();
    if (!iter.hasNext()) {
      return "$";
    }

    var sb = new StringBuilder("$");
    while (iter.hasNext()) {
      var element = iter.next();
      var pathComponent = element.pathComponent();
      if (!pathComponent.isBlank()) {
        sb.append(pathComponent);
      }
    }

    return sb.toString();
  }
}
