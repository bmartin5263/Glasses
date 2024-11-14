package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.lens.LensRuntime;
import dev.bdon.glasses.path.DynamicNode;
import dev.bdon.glasses.path.Path;
import dev.bdon.glasses.path.PathNode;
import dev.bdon.glasses.type.Property;

import java.util.Deque;

public abstract non-sealed class SelectionElement<I, O> extends Element<I, O> {
  public SelectionElement() {
  }

  public SelectionElement(Element<?, ?> parent) {
    super(parent);
  }

  @Override
  public void buildPath(LensRuntime runtime, Path path) {
    path.append(pathNode(runtime));
  }

  public abstract PathNode pathNode(LensRuntime runtime);

  public abstract Property<I, O> property(Deque<DynamicNode> components);
}
