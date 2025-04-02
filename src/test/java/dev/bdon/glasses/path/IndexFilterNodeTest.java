package dev.bdon.glasses.path;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class IndexFilterNodeTest {

  @Test
  void constructor() {
    var expression = "expression";
    var node = new IndexFilterNode(expression);
    assertThat(node.expression()).isEqualTo(expression);
    assertThat(node.toString()).isEqualTo("[%s]", expression);
  }

  @Test
  void accept() {
    var pathWrittenInvoked = new AtomicBoolean();
    var pathWriter = new PathWriter() {
      @Override public void writeRoot(StringBuilder path) {}
      @Override public void write(FieldNode node, StringBuilder path) {}
      @Override public void write(IndexFilterNode node, StringBuilder path) { pathWrittenInvoked.set(true); }
      @Override public void write(IndexNode node, StringBuilder path) {}
    };
    var node = new IndexFilterNode("expression");
    var sb = new StringBuilder();

    node.accept(pathWriter, sb);

    assertThat(pathWrittenInvoked).isTrue();
  }

}