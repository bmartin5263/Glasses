package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.DynamicComponent;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.type.IProperty;
import dev.bdon.glasses.type.IndexedProperty;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.util.Assert;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

public class SelectAtElement<O> extends Element<List<O>, O> {
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
  public Blurs<O> apply(LensRuntime runtime, Blur<List<O>> blur) {
    return Blurs.of(blur.nextAtIndex(listComponentType, index));
  }

  @Override
  public String pathComponent() {
    return "[%s]".formatted(index);
  }

  @Override
  public Optional<IProperty<List<O>, O>> property(Deque<DynamicComponent> components) {
    return Optional.of(new IndexedProperty<>(listComponentType.javaClass(), index));
  }
}
