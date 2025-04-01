package dev.bdon.glasses.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AssertTest {

  @Test
  @SuppressWarnings("ConstantConditions")
  void nonNull() {
    assertThatNoException().isThrownBy(() -> Assert.nonNull(42, "Value cannot be null: ", 42));
    assertThatException()
        .isThrownBy(() -> Assert.nonNull(null, "%s cannot be null", "number"))
        .withMessage("number cannot be null");


    assertThatNoException().isThrownBy(() -> Assert.nonNull(42, ArithmeticException::new, "Value cannot be null: ", 42));
    assertThatExceptionOfType(ArithmeticException.class)
        .isThrownBy(() -> Assert.nonNull(null, ArithmeticException::new, "%s cannot be null", "number"))
        .withMessage("number cannot be null");
  }


  @Test
  @SuppressWarnings("ConstantConditions")
  void nonNullArgument() {
    assertThatNoException().isThrownBy(() -> Assert.nonNullArgument(42, "number"));
    assertThatException()
        .isThrownBy(() -> Assert.nonNullArgument(null, "number"))
        .withMessage("number must not be null");


    assertThatNoException().isThrownBy(() -> Assert.nonNullArgument(42, ArithmeticException::new, "number"));
    assertThatExceptionOfType(ArithmeticException.class)
        .isThrownBy(() -> Assert.nonNullArgument(null, ArithmeticException::new, "number"))
        .withMessage("number must not be null");
  }


  @Test
  void nonNegativeArgument() {
    assertThatNoException().isThrownBy(() -> Assert.nonNegativeArgument(5, "test value"));
    assertThatException()
        .isThrownBy(() -> Assert.nonNegativeArgument(-1, "test value"))
        .withMessage("test value must be greater than or equal to zero: -1");
  }

}