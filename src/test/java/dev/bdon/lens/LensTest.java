package dev.bdon.lens;

import dev.bdon.lens.model.Address;
import dev.bdon.lens.model.Library;
import dev.bdon.lens.model.Line;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class LensTest {

  @Test
  void focus_noElements() {
    var lens = Lens.create(String.class);

    var result = lens.focus("a string");

    assertAll(
        () -> assertThat(result.value()).isEqualTo("a string"),
        () -> assertThat(result.path()).isEqualTo("$")
    );
    assertThatExceptionOfType(LensExecutionException.class)
        .isThrownBy(() -> result.override("some other string"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = "Chicago")
  void focus_twoSelects(String value) {
    var lens = Lens.create(Library.class)
        .select(Library::setAddress, Address.class)
        .select(Address::setCity, String.class);
    var target = new Library().setAddress(new Address().setCity(value));

    assertThat(lens.path()).isEqualTo("$.address.city");

    var result1 = lens.focus(target);
    assertAll(
        () -> assertThat(result1.value()).isEqualTo(value),
        () -> assertThat(result1.path()).isEqualTo("$.address.city")
    );

    target.setAddress(null);
    var result2 = lens.focus(target);
    assertAll(
        () -> assertThat(result2.value()).isNull(),
        () -> assertThat(result2.path()).isEqualTo("$.address.city")
    );
  }

  @Test
  void focus_oneSelect_oneSelectAll_oneSelect() {
    var lens = Lens.create(Library.class)
        .select(Library::setAddress, Address.class)
        .selectAll(Address::setLines, Line.class)
        .select(Line::setText, String.class);
    var target = new Library()
        .setAddress(new Address()
            .setLines(new ArrayList<>(List.of(
                new Line().setText("line1"),
                new Line().setText("line2"),
                new Line().setText(null)
            )))
        );

    assertThat(lens.path()).isEqualTo("$.address.lines[*].text");

    var result1 = lens.focus(target);
    assertAll(
        () -> assertThat(result1.get(0).value()).isEqualTo("line1"),
        () -> assertThat(result1.get(0).path()).isEqualTo("$.address.lines[0].text"),
        () -> assertThat(result1.get(1).value()).isEqualTo("line2"),
        () -> assertThat(result1.get(1).path()).isEqualTo("$.address.lines[1].text"),
        () -> assertThat(result1.get(2).value()).isNull(),
        () -> assertThat(result1.get(2).path()).isEqualTo("$.address.lines[2].text")
    );

    target.setAddress(null);
    var result2 = lens.focus(target);
    assertAll(
        () -> assertThat(result2).isEmpty()
    );
  }

//  private static class LensTestParams {
//    private
//  }
//
//  private static Stream<Arguments> provideStringsForIsBlank() {
//    return Stream.of(
//        Arguments.of(null, true),
//        Arguments.of("", true),
//        Arguments.of("  ", true),
//        Arguments.of("not blank", false)
//    );
//  }
}