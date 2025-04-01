package dev.bdon.glasses.type;

import dev.bdon.glasses.util.LensInternalException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FieldPropertyTest {

  @Test
  void from_withGenericJavaField_shouldThrow() throws Exception {
    var field = InvalidClass.class.getField("someField");
    assertThatExceptionOfType(LensInternalException.class)
        .isThrownBy(() -> FieldProperty.from(new JavaField(field)))
        .withMessage("Generic fields not supported: JavaField[class=%s, name=someField, type=T]", InvalidClass.class);
  }

  private static class InvalidClass<T> {
    public T someField;
  }
}