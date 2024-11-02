package dev.bdon.lens;

import java.util.List;
import java.util.function.Function;

public class Blur<T> {
  private Lens<?, ?> lens;
  private final T value;
  private final Integer index;
  private final Route route;
  private final boolean dead;

  private Blur(Lens<?, ?> lens, T value, Integer index, Route route, boolean dead) {
    this.value = Assert.nonNull(value, "Blur value cannot be null");
    this.index = index;
    this.route = route;
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

  public <X> Blur<X> next(Function<T, X> mapper, String pathComponent, X deadValue) {
    X nextValue = deadValue;
    boolean isDead = true;
    if (!isDead()) {
      var mappedValue = mapper.apply(value);
      if (mappedValue != null) {
        nextValue = mappedValue;
        isDead = false;
      }
    }
    return new Blur<>(lens, nextValue, null, route.addComponent(pathComponent), isDead);
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
