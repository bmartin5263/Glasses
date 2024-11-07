package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.SelectAllElement;
import dev.bdon.glasses.lens.element.SelectAtElement;
import dev.bdon.glasses.lens.element.SelectElement;
import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.util.Assert;
import dev.bdon.glasses.util.Setter;

import java.util.List;

class LensImpl {

  static List<Image<Object>> focus(Lens<Object, Object> lens, Object target) {
    Assert.nonNullArgument(target, "target", LensInternalException::new);

    var runtime = new LensRuntime(lens);
    lens.context().defaultLensConfiguration().accept(runtime);

    var blurs = Blurs.of(Blur.root(lens, target));

    var elements = LensUtils.reverseElementChain(lens.leaf()).toList();
    for (var element : elements) {
      var nextBlurs = new Blurs<>();
      for (var blur : blurs) {
        nextBlurs.addAll(element.apply(runtime, blur));
      }
      blurs = nextBlurs;
    }

    return blurs.stream()
        .map(b -> b.toImage(target))
        .toList();
  }

  static <O> void override(InternalLens<O> lens, Image<O> image, O newValue) {
    Assert.nonNullArgument(lens, "lens", LensInternalException::new);
    Assert.nonNullArgument(image, "image", LensInternalException::new);

    var current = image.target();
    var properties = lens.properties(image.route()).toList();
    var propertyIter = properties.iterator();

    if (!propertyIter.hasNext()) {
      throw new LensExecutionException("Cannot call override() on an image produce by an empty Lens");
    }
    while (propertyIter.hasNext()) {
      var property = propertyIter.next();
      if (!propertyIter.hasNext()) {
        property.set(current, newValue);
      }
      else {
        var next = property.get(current);
        if (next == null) {
          next = LensUtils.newTracer(property.type());
          property.set(current, next);
        }
        current = next;
      }
    }
  }

  static <I, O, X, L extends Lens<I, X>> L select(
      InternalLens<O> lens,
      Setter<O, X> setter,
      Class<X> type,
      LensConstructor<I, X, L> constructor
  ) {
    var target = LensUtils.newTracer(lens.outputType());
    var property = lens.context().findProperty(target, setter, type);
    var next = new SelectElement<>(lens.leaf(), property);
    return constructor.construct(lens.context(), type, next);
  }

  static <I, O, X, L extends Lens<I, X>> L selectFirst(
      InternalLens<O> lens,
      Setter<O, List<X>> setter,
      Class<X> type,
      LensConstructor<I, X, L> constructor
  ) {
    var target = LensUtils.newTracer(lens.outputType());
    var listClass = LensImpl.<X>listClass();
    var listItemType = lens.context().findType(type);
    var listProperty = lens.context().findProperty(target, setter, listClass);
    var next = new SelectAtElement<>(new SelectElement<>(lens.leaf(), listProperty), listItemType, 0);
    return constructor.construct(lens.context(), type, next);
  }

  static <I, O, X> PolyLens<I, X> selectAll(
      InternalLens<O> lens,
      Setter<O, List<X>> setter,
      Class<X> type
  ) {
    O target = LensUtils.newTracer(lens.outputType());
    Class<List<X>> listClass = listClass();
    Property<O, List<X>> listProperty = lens.context().findProperty(target, setter, listClass);
    Type<X> listItemType = lens.context().findType(type);
    SelectAllElement<X> next = new SelectAllElement<>(new SelectElement<>(lens.leaf(), listProperty), listItemType);
    return new PolyLens<>(lens.context(), type, next);
  }

  @SuppressWarnings("unchecked")
  static <T> Class<List<T>> listClass() {
    return (Class<List<T>>)(Class<?>) List.class;
  }
}
