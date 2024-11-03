package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.SelectElement;
import dev.bdon.glasses.util.Assert;
import dev.bdon.glasses.util.Setter;

import java.util.List;

public class LensImpl {

  public static List<Image<Object>> focus(Lens<Object, Object> lens, Object target) {
    Assert.nonNull(target, "target", LensExecutionException::new);
    var runtime = new LensRuntime(lens);
    var blurs = Blurs.of(Blur.root(lens, target));

    var elements = LensUtils.gatherElements(lens.leaf());
    for (var element : elements) {
      var nextBlurs = new Blurs<>();
      for (var blur : blurs) {
        nextBlurs.addAll(element.apply(runtime, blur));
      }
      blurs = nextBlurs;
    }

    return blurs.stream()
        .map(Blur::toImage)
        .toList();
  }

  public static <I, O, X, L extends Lens<I, X>> L select(
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

}
