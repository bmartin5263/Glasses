package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.Blur;
import dev.bdon.glasses.lens.Blurs;
import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.path.DynamicNode;
import dev.bdon.glasses.path.FieldNode;
import dev.bdon.glasses.path.PathNode;
import dev.bdon.glasses.type.FieldProperty;
import dev.bdon.glasses.type.Property;
import dev.bdon.glasses.type.Tracers;
import dev.bdon.glasses.util.Assert;

import java.util.Deque;

public class SelectElement<I, O> extends SelectionElement<I, O> {
  private final FieldProperty<I, O> property;

  public SelectElement(
      Element<?, ?> parent,
      FieldProperty<I, O> property
  ) {
    super(parent);
    this.property = Assert.nonNullArgument(property, "property");
  }

  @Override
  public Blurs<O> apply(LensRuntime runtime, Blur<I> blur) {
    var pathNode = pathNode(runtime);
    if (blur.isDead()) {
      return Blurs.of(kill(blur, pathNode));
    }
    var nextValue = property.get(blur.value());
    if (nextValue == null) {
      return Blurs.of(kill(blur, pathNode));
    }
    return Blurs.of(
        new Blur<>(blur.lens(), nextValue, blur.path().append(pathNode))
    );
  }

  private Blur<O> kill(Blur<I> blur, PathNode pathNode) {
    O tracer = Tracers.newTracer(property.type());
    return new Blur<>(blur.lens(), tracer, blur.path().append(pathNode), true);
  }

  @Override
  public PathNode pathNode(LensRuntime runtime) {
    return new FieldNode(runtime.extractFieldPath(property));
  }

  @Override
  public Property<I, O> property(Deque<DynamicNode> components) {
    return property;
  }
}
