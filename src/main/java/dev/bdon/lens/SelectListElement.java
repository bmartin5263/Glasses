package dev.bdon.lens;

import java.util.ArrayList;
import java.util.List;

public class SelectListElement<I, O> extends Element<I, List<O>> {
  private final Getter<I, List<O>> getter;
  private final Setter<I, List<O>> setter;
  private final String pathComponent;

  public <X, Y> SelectListElement(Element<X, Y> parent, Getter<I, List<O>> getter, I inputTracer) {
    this(parent, getter, null, inputTracer);
  }

  public <X, Y> SelectListElement(Element<X, Y> parent, Getter<I, List<O>> getter, Setter<I, List<O>> setter, I inputTracer) {
    super(parent);
    this.getter = Assert.nonNull(getter, "getter");
    this.setter = setter;

    Assert.nonNull(inputTracer, "inputTracer");
    var original = getter.get(inputTracer);
    var field = ReflectionUtils.findFieldWithValueOn(inputTracer, original);
    pathComponent = "." + field.getName();
  }

  @Override
  public Blurs<List<O>> apply(Blur<I> blur) {
    return Blurs.single(blur.next(getter, pathComponent, new ArrayList<>()));
  }

  @Override
  public List<O> overwrite(I input, List<O> newValue) {
    return null;
  }

  @Override
  public List<O> overwriteWithDefaultIfNull(I input) {
    return null;
  }

  @Override
  public String pathComponent() {
    return pathComponent;
  }
}
