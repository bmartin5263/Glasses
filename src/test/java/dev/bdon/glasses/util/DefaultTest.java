package dev.bdon.glasses.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NullSafeTest {

  @Test
  void toEmpty() {
    assertThat(NullSafe.<Object, List<Object>>copy(null)).isEmpty();
    assertThat(NullSafe.copy(List.of())).isEmpty();
    assertThat(NullSafe.copy(List.of("value"))).isEqualTo(List.of("value"));
  }

}