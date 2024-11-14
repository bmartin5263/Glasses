package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;
import dev.bdon.glasses.path.Path;
import dev.bdon.glasses.util.Setter;

import java.util.List;
import java.util.function.Consumer;

public sealed interface Lens<I, O> permits MonoLens, PolyLens {
  // Building Methods
  Lens<I, O> configure(Consumer<LensConfigurationBuilder> configurer);
  <X> Lens<I, X> select(Setter<O, X> setter, Class<X> type);
  <X> Lens<I, X> selectFirst(Setter<O, List<X>> setter, Class<X> type);
  <X> PolyLens<I, X> selectAll(Setter<O, List<X>> getter, Class<X> type);

  // Execution Methods
  List<Image<O>> focusToList(I target);
  void override(Image<O> image, O newValue);

  // Factory Methods
  static ConfiguredContext configureRoot(Consumer<LensConfigurationBuilder> configurer) {
    return LensContext.DEFAULT.configure(configurer);
  }

  static <T> MonoLens<T, T> create(Class<T> type) {
    return LensContext.DEFAULT.createLens(type);
  }

  static LensContext createContext() {
    return new LensContext(LensConfiguration.DEFAULT);
  }

  static LensContext createContext(Consumer<LensConfigurationBuilder> configurer) {
    var builder = new LensConfigurationBuilder();
    configurer.accept(builder);
    return new LensContext(builder.build());
  }

  // Accessors
  LensContext context();
  <X> Element<X, O> leaf(); // TODO - hide from interface
  Class<O> outputType();

  default Path path() {
    return LensImpl.pathOf(this);
  }
}
