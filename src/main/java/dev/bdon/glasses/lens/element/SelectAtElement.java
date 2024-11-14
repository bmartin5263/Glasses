package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.*;
import dev.bdon.glasses.path.DynamicNode;
import dev.bdon.glasses.path.IndexNode;
import dev.bdon.glasses.path.PathNode;
import dev.bdon.glasses.type.ListItemProperty;
import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.type.Type;
import dev.bdon.glasses.util.Assert;

import java.util.Deque;
import java.util.List;

public class SelectAtElement<O> extends SelectionElement<List<O>, O> {
  private final ListItemProperty<O> property;
  private final PathNode pathNode;

  public SelectAtElement(
      Element<?, ?> parent,
      Type<O> listItemType,
      int index
  ) {
    super(parent);
    this.property = new ListItemProperty<>(listItemType.javaClass(), index);
    this.pathNode = new IndexNode(index);
  }

  @Override
  public Blurs<O> apply(LensRuntime runtime, Blur<List<O>> blur) {
    if (blur.isDead()) {
      return Blurs.of(kill(blur));
    }
    var list = blur.value();
    if (property.index() >= list.size()) {
      return Blurs.of(kill(blur));
    }
    var nextValue = (O) list.get(property.index());
    Assert.nonNull(nextValue, LensConventionViolatedException::new, "Lists cannot have null items");
    return Blurs.of(
        new Blur<>(blur.lens(), nextValue, blur.path().append(pathNode), false)
    );
  }

  private Blur<O> kill(Blur<List<O>> blur) {
    O tracer = LensUtils.newTracer(property.type());
    return new Blur<>(blur.lens(), tracer, blur.path().append(pathNode), true);
  }

  @Override
  public PathNode pathNode(LensRuntime runtime) {
    return pathNode;
  }

  @Override
  public Property<List<O>, O> property(Deque<DynamicNode> components) {
    return property;
  }
}
