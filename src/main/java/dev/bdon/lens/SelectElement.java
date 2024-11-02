package dev.bdon.lens;

public class SelectElement<I, O> extends Element<I, O> {
  private final Getter<I, O> getter;
  private final Setter<I, O> setter;
  private final Class<O> outputType;
  private final String pathComponent;

  public SelectElement(
      Element<?, ?> parent,
      Getter<I, O> getter,
      Setter<I, O> setter,
      Class<I> inputType,
      Class<O> outputType
  ) {
    super(parent);
    this.getter = Assert.nonNull(getter, "getter");
    this.setter = Assert.nonNull(setter, "setter");
    this.outputType = Assert.nonNull(outputType, "outputType");

    Assert.nonNull(inputType, "inputType");
    var inputTracer = LensUtils.getTracer(inputType);
    var outputTracer = LensUtils.getTracer(outputType);

    var original = getter.get(inputTracer);
    setter.set(inputTracer, outputTracer);
    var field = ReflectionUtils.findFieldWithValueOn(inputTracer, outputTracer);
    setter.set(inputTracer, original);
    pathComponent = "." + field.getName();
  }

  @Override
  public Blurs<O> apply(Blur<I> blur) {
    return Blurs.single(blur.next(getter, pathComponent, LensUtils.getTracer(outputType)));
  }

  @Override
  public O overwrite(I input, O newValue) {
    var previousValue = getter.get(input);
    setter.set(input, newValue);
    return previousValue;
  }

  @Override
  public O overwriteWithDefaultIfNull(I input) {
    var currentValue = getter.get(input);
    if (currentValue == null) {
      currentValue = LensUtils.getTracer(outputType);
      setter.set(input, currentValue);
    }
    return currentValue;
  }

  @Override
  public String pathComponent() {
    return pathComponent;
  }
}
