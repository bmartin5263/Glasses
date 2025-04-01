package dev.bdon.glasses.path;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    var pathWriter = mock(PathWriter.class);
    var node = new IndexFilterNode("expression");
    var sb = new StringBuilder();

    node.accept(pathWriter, sb);

    verify(pathWriter).write(node, sb);
  }

}