package dev.bdon.glasses.path;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class IndexNodeTest {

  @Test
  void constructor() {
    var index = 1;
    var node = new IndexNode(index);
    assertThat(node.index()).isEqualTo(index);
    assertThat(node.toString()).isEqualTo("[%s]", index);
  }

  @Test
  void accept() {
    var pathWrittenInvoked = new AtomicBoolean();
    var pathWriter = new PathWriter() {
      @Override public void writeRoot(StringBuilder path) {}
      @Override public void write(FieldNode node, StringBuilder path) {}
      @Override public void write(IndexFilterNode node, StringBuilder path) {}
      @Override public void write(IndexNode node, StringBuilder path) { pathWrittenInvoked.set(true); }
    };
    var node = new IndexNode(1);
    var sb = new StringBuilder();

    node.accept(pathWriter, sb);

    assertThat(pathWrittenInvoked).isTrue();
  }

}