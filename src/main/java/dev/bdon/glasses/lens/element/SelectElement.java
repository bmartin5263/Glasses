package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.lens.LensUtils;
import dev.bdon.glasses.path.DynamicNode;
import dev.bdon.glasses.path.FieldNode;
import dev.bdon.glasses.path.PathNode;
import dev.bdon.glasses.type.IProperty;
import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.util.Assert;

import java.util.Deque;

public class SelectElement<I, O> extends SelectionElement<I, O> {
  private final Property<I, O> property;
  private final PathNode pathNode;

  public SelectElement(
      Element<?, ?> parent,
      Property<I, O> property
  ) {
    super(parent);
    this.property = Assert.nonNullArgument(property, "property");
    this.pathNode = new FieldNode(property.name());
  }

  @Override
  public Blurs<O> apply(LensRuntime runtime, Blur<I> blur) {
    if (blur.isDead()) {
      return Blurs.of(kill(blur));
    }
    var nextValue = property.get(blur.value());
    if (nextValue == null) {
      return Blurs.of(kill(blur));
    }
    return Blurs.of(
        new Blur<>(blur.lens(), nextValue, blur.path().append(pathNode))
    );
  }

  private Blur<O> kill(Blur<I> blur) {
    O tracer = LensUtils.newTracer(property.type());
    return new Blur<>(blur.lens(), tracer, blur.path().append(pathNode), true);
  }

  @Override
  public PathNode pathNode() {
    return pathNode;
  }

  @Override
  public IProperty<I, O> property(Deque<DynamicNode> components) {
    return property;
  }
}
