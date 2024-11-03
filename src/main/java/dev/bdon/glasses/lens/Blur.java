package dev.bdon.glasses.lens;

import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.util.Assert;

import java.util.List;

public class Blur<T> {
  private Lens<?, ?> lens;
  private final T value;
  private final Integer index;
  private final Route route;
  private final boolean dead;

  private Blur(Lens<?, ?> lens, T value, Integer index, Route route, boolean dead) {
    this.lens = Assert.nonNullArgument(lens, "lens");
    this.value = Assert.nonNull(value, "value cannot be null. Use dead=true and a non-null value to represent null");
    this.index = index;
    this.route = Assert.nonNullArgument(route, "route");;
    this.dead = dead;
  }

  public static <T> Blur<T> root(Lens<?, ?> lens, T value) {
    return new Blur<>(lens, value, null, Route.start(), false);
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

  public <X> Blur<X> nextAtIndex(Type<X> listComponentType, int index) {
    if (isDead()) {
      return killed(listComponentType, index);
    }
    if (value instanceof List<?> list) {
      if (index >= list.size()) {
        return killed(listComponentType, index);
      }
      var nextValue = (X) list.get(index);
      Assert.nonNull(nextValue, LensConventionBrokenException::new, "Lists cannot have null items");
      return new Blur<>(lens, nextValue, index, route.addComponent("[%s]".formatted(index)), false);
    }
    throw new LensInternalException("divide() called on a Blur not holding a List. this = %s", this);
  }

  @SuppressWarnings("unchecked")
  public <X> Blurs<X> divide() {
    if (value instanceof List<?> list) {
      var blurs = new Blurs<X>();
      for (int i = 0; i < list.size(); ++i) {
        blurs.add(new Blur<>(lens, (X) list.get(i), i, route.addComponent("[%s]".formatted(i)), false));
      }
      return blurs;
    }
    throw new LensInternalException("divide() called on a Blur not holding a List. this = %s", this);
  }

  private <I, O> Blur<O> killed(Property<I, O> deadProperty) {
    O tracer = LensUtils.newTracer(deadProperty.type());
    return new Blur<>(lens, tracer, null, route.addComponent("." + deadProperty.name()), true);
  }

  private <I, O> Blur<O> killed(Type<O> listComponentType, int index) {
    O tracer = listComponentType.instantiate();
    return new Blur<>(lens, tracer, index, route.addComponent("[%s]".formatted(index)), true);
  }

}
