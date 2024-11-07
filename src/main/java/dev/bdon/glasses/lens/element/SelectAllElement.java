package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensInternalException;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.path.DynamicNode;
import dev.bdon.glasses.path.IndexFilterNode;
import dev.bdon.glasses.path.IndexNode;
import dev.bdon.glasses.path.PathNode;
import dev.bdon.glasses.type.IProperty;
import dev.bdon.glasses.type.IndexedProperty;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.util.Assert;

import java.util.Deque;
import java.util.List;

public class SelectAllElement<O> extends SelectionElement<List<O>, O> {
  private final Type<O> itemType;
  private final PathNode pathNode = new IndexFilterNode("*");

  public SelectAllElement(
      Element<?, ?> parent,
      Type<O> itemType
  ) {
    super(parent);
    this.itemType = Assert.nonNullArgument(itemType, "itemType");
  }

  @Override
  public Blurs<O> apply(LensRuntime runtime, Blur<List<O>> blur) {
    var blurs = new Blurs<O>();
    var list = blur.value();
    for (int index = 0; index < list.size(); ++index) {
      var path = blur.path().copy();
      blurs.add(new Blur<>(blur.lens(), list.get(index), path.append(new IndexNode(index)), false));
    }
    return blurs;
  }

  @Override
  public PathNode pathNode() {
    return pathNode;
  }

  @Override
  public IProperty<List<O>, O> property(Deque<DynamicNode> components) {
    if (components.isEmpty()) {
      throw new LensInternalException("No more dynamic components to determine the index");
    }
    var nextComponent = components.pop();
    if (nextComponent instanceof IndexNode indexComponent) {
      return new IndexedProperty<>(itemType.javaClass(), indexComponent.index());
    }
    throw new LensInternalException("Unhandled dynamic component type: %s", nextComponent.getClass());
  }
}
