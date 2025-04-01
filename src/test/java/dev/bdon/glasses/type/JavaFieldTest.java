package dev.bdon.glasses.type;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaFieldTest {

  @Test
  void get_shouldReturnValue() throws Exception {
    var field = SomeClass.class.getDeclaredField("someField");
    field.setAccessible(true);
    var javaField = new JavaField(field);
    var target = new SomeClass("my value");

    assertThat(javaField.<SomeClass, String>get(target)).isEqualTo("my value");
  }

  @Test
  void set_shouldReturnValue() throws Exception {
    var field = SomeClass.class.getDeclaredField("someField");
    field.setAccessible(true);
    var javaField = new JavaField(field);
    var target = new SomeClass("my value");

    javaField.set(target, "another value");

    assertThat(target.getSomeField()).isEqualTo("another value");
  }

//  @Test
//  void get_shouldThrowIfInaccessible() throws Exception {
//    var field = SomeClass.class.getDeclaredField("someField");
//    field.setAccessible(false);
//    var javaField = new JavaField(field);
//    var target = new SomeClass("my value");
//
//    assertThatExceptionOfType(LensInternalException.class)
//        .isThrownBy(() -> javaField.get(target))
//        .withMessage("asdf");
//  }

}