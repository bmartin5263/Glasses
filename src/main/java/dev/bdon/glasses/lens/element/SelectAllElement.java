package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.*;
import dev.bdon.glasses.type.IProperty;
import dev.bdon.glasses.type.IndexedProperty;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.util.Assert;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

public class SelectAllElement<O> extends Element<List<O>, O> {
  private final Type<O> listComponentType;

  public SelectAllElement(
      Element<?, ?> parent,
      Type<O> listComponentType
  ) {
    super(parent);
    this.listComponentType = Assert.nonNullArgument(listComponentType, "listComponentType");
  }

  @Override
  public Blurs<O> apply(LensRuntime runtime, Blur<List<O>> blur) {
    return blur.divide();
  }

  @Override
  public String pathComponent() {
    return "[*]";
  }

  @Override
  public Optional<IProperty<List<O>, O>> property(Deque<DynamicComponent> components) {
    if (components.isEmpty()) {
      throw new LensInternalException("No more dynamic components to determine the index");
    }
    var nextComponent = components.pop();
    if (nextComponent instanceof IndexComponent indexComponent) {
      return Optional.of(new IndexedProperty<>(listComponentType.javaClass(), indexComponent.index()));
    }
    throw new LensInternalException("Unhandled dynamic component type: %s", nextComponent.getClass());
  }
}
