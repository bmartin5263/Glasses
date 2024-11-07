package dev.bdon.glasses.path;

public class IndexNode implements DynamicNode {
  private final int index;

  public IndexNode(int index) {
    this.index = index;
  }

  public int index() {
    return index;
  }

  @Override
  public String toString() {
    return "[%s]".formatted(index);
  }

  @Override
  public void accept(PathWriter pathWriter, StringBuilder writer) {
    pathWriter.write(this, writer);
  }
}
