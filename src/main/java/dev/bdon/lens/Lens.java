package dev.bdon.lens;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public sealed interface Lens<I, O> permits MonoLens, PolyLens {
  // Building Methods
  <X> Lens<I, X> select(Getter<O, X> getter, Setter<O, X> setter, Class<X> type);
  <X, L extends List<X>> Lens<I, X> selectFirst(Getter<O, L> getter);
  <X> PolyLens<I, X> selectAll(Getter<O, List<X>> getter, Class<X> type);

  // Execution Methods
  List<Image<O>> focusToList(I target);

  // Factory Methods
  static <T> MonoLens<T, T> create(Class<T> type) {
    return LensFactory.DEFAULT.create(type);
  }

  // Accessors
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
