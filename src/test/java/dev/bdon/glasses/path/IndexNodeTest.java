package dev.bdon.glasses.path;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    var pathWriter = mock(PathWriter.class);
    var node = new IndexNode(1);
    var sb = new StringBuilder();

    node.accept(pathWriter, sb);

    verify(pathWriter).write(node, sb);
  }

}