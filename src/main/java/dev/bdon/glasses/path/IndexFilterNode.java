package dev.bdon.glasses.path;

public class IndexFilterNode implements DynamicNode {
  private final String expression;

  public IndexFilterNode(String expression) {
    this.expression = expression;
  }

  public String expression() {
    return expression;
  }

  @Override
  public String toString() {
    return "[%s]".formatted(expression);
  }

  @Override
  public void accept(PathWriter pathWriter, StringBuilder writer) {
    pathWriter.write(this, writer);
  }
}
