package dev.bdon.lens;

import java.util.function.Function;

public interface Getter<I, O> extends Function<I, O> {
  default O get(I target) {
    return apply(target);
  }
}
