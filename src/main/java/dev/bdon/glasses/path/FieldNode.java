package dev.bdon.glasses.path;

public class FieldNode implements PathNode {
  private final String name;

  public FieldNode(String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public void accept(PathWriter pathWriter, StringBuilder writer) {
    pathWriter.write(this, writer);
  }
}
