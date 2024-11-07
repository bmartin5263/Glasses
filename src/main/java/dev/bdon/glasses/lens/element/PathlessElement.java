package dev.bdon.glasses.lens.element;

public abstract non-sealed class PathlessElement<I, O> extends Element<I, O> {
  public PathlessElement() {
  }

  public PathlessElement(Element<?, ?> parent) {
    super(parent);
  }
}
