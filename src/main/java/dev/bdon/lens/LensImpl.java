package dev.bdon.lens;

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

}
