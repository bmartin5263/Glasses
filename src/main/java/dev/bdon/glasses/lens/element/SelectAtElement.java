package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.util.Assert;

import java.util.List;

public class SelectAtElement<I, O> extends Element<List<I>, O> {
  private final Type<O> listComponentType;
  private final int index;

  public SelectAtElement(
      Element<?, ?> parent,
      Type<O> listComponentType,
      int index
  ) {
    super(parent);
    this.listComponentType = Assert.nonNullArgument(listComponentType, "listComponentType");
    this.index = Assert.positiveOrZeroArgument(index, "index");
  }

  @Override
  public Blurs<O> apply(LensRuntime runtime, Blur<List<I>> blur) {
    return Blurs.of(blur.nextAtIndex(listComponentType, index));
  }

  @Override
  public String pathComponent() {
    return "[%s]".formatted(index);
  }
}
