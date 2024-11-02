package dev.bdon.lens;

import java.util.List;

public class LensImpl {

  public static List<Image<Object>> focus(Lens<Object, Object> lens, Object target) {
    Assert.nonNull(target, "target", LensExecutionException::new);
    var blurs = Blurs.single(Blur.root(lens, target));

    var elements = LensUtils.gatherElements(lens.leaf());
    for (var element : elements) {
      var nextBlurs = new Blurs<>();
      for (var blur : blurs) {
        nextBlurs.addAll(element.apply(blur));
      }
      blurs = nextBlurs;
    }

    return blurs.stream()
        .map(Blur::toImage)
        .toList();
  }

  public static <I, O> O overwrite(Lens<Object, Object> lens, I target, O newValue) {
    Assert.nonNull(target, "target", LensExecutionException::new);
    Assert.nonNull(lens, "lens");
    Assert.nonNull(lens.leaf(), LensExecutionException::new, "overwrite() cannot be called on Images produced from empty Lenses");

    Object current = target;
    var iter = LensUtils.gatherElements(lens.leaf()).iterator();
    while (iter.hasNext()) {
      var element = iter.next();
      if (iter.hasNext()) {
        current = element.overwriteWithDefaultIfNull(current);
      }
      else {
        return (O) element.overwrite(current, newValue);
      }
    }

    throw new LensInternalException("Should never get here");
  }

}
