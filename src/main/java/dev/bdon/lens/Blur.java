package dev.bdon.lens;

import java.util.List;

public class Blur<T> {
  private Lens<?, ?> lens;
  private final T value;
  private final Integer index;
  private final Route route;
  private final boolean dead;

  private Blur(Lens<?, ?> lens, T value, Integer index, Route route, boolean dead) {
    this.lens = Assert.argumentNonNull(lens, "lens");
    this.value = Assert.nonNull(value, "value cannot be null. Use dead=true and a non-null value to represent null");
    this.index = index;
    this.route = Assert.argumentNonNull(route, "route");;
    this.dead = dead;
  }

  public static <T> Blur<T> root(Lens<?, ?> lens, T value) {
    return new Blur<>(lens, value, null, Route.start(), false);
  }

  public <I, O> Blur<O> killed(Property<I, O> deadProperty) {
    O tracer = LensUtils.newTracer(deadProperty.type());
    return new Blur<>(lens, tracer, null, route.addComponent("." + deadProperty.name()), true);
  }

  public Image<T> toImage() {
    return new Image<>(dead ? null : value, route);
  }

  public T value() {
    return value;
  }

  public Route route() {
    return route;
  }

  public boolean isDead() {
    return dead;
  }

  public <X> Blur<X> next(Property<T, X> property) {
    if (isDead()) {
      return killed(property);
    }
    var nextValue = property.get(value);
    if (nextValue == null) {
      return killed(property);
    }
    return new Blur<>(lens, nextValue, null, route.addComponent("." + property.name()), false);
  }

  @SuppressWarnings("unchecked")
  public <X> Blurs<X> divide() {
    if (value == null) {
      return new Blurs<>();
    }
    if (value instanceof List<?> list) {
      var blurs = new Blurs<X>();
      for (int i = 0; i < list.size(); ++i) {
        blurs.add(new Blur<>(lens, (X) list.get(i), i, route.addComponent("[%s]".formatted(i)), false));
      }
      return blurs;
    }
    throw new LensInternalException("divide() called on a Blur not holding a List. this = %s", this);
  }

}
