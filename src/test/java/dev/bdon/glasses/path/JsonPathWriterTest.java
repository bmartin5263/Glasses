package dev.bdon.glasses.path;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonPathWriterTest {

  @Test
  void writePath() {
    var subject = new JsonPathWriter();
    var path = new Path()
        .append(new FieldNode("hello"))
        .append(new FieldNode("world"));

    var result = subject.write(path);

    assertThat(result).isEqualTo("$.hello.world");
  }

  @Test
  void writePath_includingSpecialCharacters() {
    var subject = new JsonPathWriter();
    var path = new Path()
        .append(new FieldNode("this-has-a-special-character"))
        .append(new FieldNode("so{does}this"));

    var result = subject.write(path);

    assertThat(result).isEqualTo("$['this-has-a-special-character']['so{does}this']");
  }

}