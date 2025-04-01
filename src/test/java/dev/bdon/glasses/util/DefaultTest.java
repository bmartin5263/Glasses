package dev.bdon.glasses.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultTest {

  @Test
  void toEmpty() {
    assertThat(Default.<Object, List<Object>>toEmpty(null)).isEmpty();
    assertThat(Default.toEmpty(List.of())).isEmpty();
    assertThat(Default.toEmpty(List.of("value"))).isEqualTo(List.of("value"));
  }

}