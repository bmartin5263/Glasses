package dev.bdon.glasses.path;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class FieldNodeTest {

  @Test
  void constructor() {
    var name = "name";
    var node = new FieldNode(name);
    assertThat(node.name()).isEqualTo(name);
    assertThat(node.toString()).isEqualTo(name);
  }

  @Test
  void accept() {
    var pathWrittenInvoked = new AtomicBoolean(false);
    var pathWriter = new PathWriter() {
      @Override public void writeRoot(StringBuilder path) {}
      @Override public void write(FieldNode node, StringBuilder path) { pathWrittenInvoked.set(true); }
      @Override public void write(IndexFilterNode node, StringBuilder path) {}
      @Override public void write(IndexNode node, StringBuilder path) {}
    };
    var node = new FieldNode("name");
    var sb = new StringBuilder();

    node.accept(pathWriter, sb);

    assertThat(pathWrittenInvoked).isTrue();
  }

}