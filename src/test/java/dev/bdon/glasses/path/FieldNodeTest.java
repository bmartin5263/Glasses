package dev.bdon.glasses.path;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    var pathWriter = mock(PathWriter.class);
    var node = new FieldNode("name");
    var sb = new StringBuilder();

    node.accept(pathWriter, sb);

    verify(pathWriter).write(node, sb);
  }

}