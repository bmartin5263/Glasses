package dev.bdon.glasses.lens.element;

import dev.bdon.glasses.path.DynamicNode;
import dev.bdon.glasses.path.PathNode;
import dev.bdon.glasses.type.IProperty;

import java.util.Deque;

public abstract non-sealed class SelectionElement<I, O> extends Element<I, O> {
  public SelectionElement() {
  }

  public SelectionElement(Element<?, ?> parent) {
    super(parent);
  }

  public abstract PathNode pathNode();

  public abstract IProperty<I, O> property(Deque<DynamicNode> components);
}
