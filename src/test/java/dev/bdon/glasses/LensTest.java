package dev.bdon.glasses;

import dev.bdon.glasses.lens.Lens;
import dev.bdon.glasses.model.*;
import dev.bdon.glasses.type.FieldProperty;
import dev.bdon.glasses.util.LensExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class LensTest {

  @Test
  void focus_noElements() {
    var lens = Lens.create(String.class);

    var result = lens.focus("a string");

    assertAll(
        () -> assertThat(result.value()).isEqualTo("a string"),
        () -> assertThat(result.path()).hasToString("$")
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

    var target = new Library()
        .setAddress(new Address()
            .setCity(value)
        );

    assertThat(lens.path()).hasToString("$.address.city");

    var result1 = lens.focus(target);
    assertAll(
        () -> assertThat(result1.value()).isEqualTo(value),
        () -> assertThat(result1.path()).hasToString("$.address.city")
    );

    result1.override("Something Else");
    assertThat(target.getAddress().getCity()).isEqualTo("Something Else");

    target.setAddress(null);
    var result2 = lens.focus(target);
    assertAll(
        () -> assertThat(result2.value()).isNull(),
        () -> assertThat(result2.path()).hasToString("$.address.city")
    );

    result2.override("Something Else");
    assertThat(target.getAddress().getCity()).isEqualTo("Something Else");
  }

  @Test
  void focus_oneSelect_oneSelectAll_oneSelect() {
    var overridden = "overridden";
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

    assertThat(lens.path()).hasToString("$.address.lines[*].text");

    var result1 = lens.focus(target);
    assertAll(
        () -> assertThat(result1.get(0).value()).isEqualTo("line1"),
        () -> assertThat(result1.get(0).path()).hasToString("$.address.lines[0].text"),
        () -> {
          result1.get(0).override(overridden);
          assertThat(target.getAddress().getLines().get(0).getText()).isEqualTo(overridden);
        },

        () -> assertThat(result1.get(1).value()).isEqualTo("line2"),
        () -> assertThat(result1.get(1).path()).hasToString("$.address.lines[1].text"),
        () -> {
          result1.get(1).override(overridden);
          assertThat(target.getAddress().getLines().get(1).getText()).isEqualTo(overridden);
        },

        () -> assertThat(result1.get(2).value()).isNull(),
        () -> assertThat(result1.get(2).path()).hasToString("$.address.lines[2].text"),
        () -> {
          result1.get(2).override(overridden);
          assertThat(target.getAddress().getLines().get(2).getText()).isEqualTo(overridden);
        }
    );

    target.setAddress(null);
    var result2 = lens.focus(target);
    assertAll(
        () -> assertThat(result2).isEmpty()
    );
  }

  @Test
  void focus_oneSelect_oneSelectFirst_oneSelect() {
    var overridden = "overridden";
    var lens = Lens.create(Library.class)
        .select(Library::setAddress, Address.class)
        .selectFirst(Address::setLines, Line.class)
        .select(Line::setText, String.class);
    var target = new Library()
        .setAddress(new Address()
            .setLines(new ArrayList<>(List.of(
                new Line().setText("line1"),
                new Line().setText("line2"),
                new Line().setText(null)
            )))
        );

    assertThat(lens.path()).hasToString("$.address.lines[0].text");

    var result1 = lens.focus(target);
    assertAll(
        () -> assertThat(result1.value()).isEqualTo("line1"),
        () -> assertThat(result1.path()).hasToString("$.address.lines[0].text"),
        () -> {
          result1.override(overridden);
          assertThat(target.getAddress().getLines().get(0).getText()).isEqualTo(overridden);
        }
    );

    target.setAddress(null);
    var result2 = lens.focus(target);
    assertAll(
        () -> assertThat(result2.value()).isEqualTo(null),
        () -> assertThat(result2.path()).hasToString("$.address.lines[0].text"),
        () -> {
          result2.override(overridden);
          assertThat(target.getAddress().getLines().get(0).getText()).isEqualTo(overridden);
        }
    );
  }

  @Test
  void focus_oneSelect_oneSelectAt_oneSelect() {
    var overridden = "overridden";
    var lens = Lens.create(Library.class)
        .select(Library::setAddress, Address.class)
        .selectAt(Address::setLines, 2, Line.class)
        .select(Line::setText, String.class);
    var target = new Library()
        .setAddress(new Address()
            .setLines(new ArrayList<>(List.of(
                new Line().setText("line1"),
                new Line().setText("line2"),
                new Line().setText(null)
            )))
        );

    assertThat(lens.path()).hasToString("$.address.lines[2].text");

    var result1 = lens.focus(target);
    assertAll(
        () -> assertThat(result1.value()).isEqualTo(null),
        () -> assertThat(result1.path()).hasToString("$.address.lines[2].text"),
        () -> {
          result1.override(overridden);
          assertThat(target.getAddress().getLines().get(2).getText()).isEqualTo(overridden);
          assertThat(target.getAddress().getLines()).hasSize(3);
        }
    );

    target.setAddress(null);
    var result2 = lens.focus(target);
    assertAll(
        () -> assertThat(result2.value()).isEqualTo(null),
        () -> assertThat(result2.path()).hasToString("$.address.lines[2].text"),
        () -> {
          result2.override(overridden);
          assertThat(target.getAddress().getLines().get(2).getText()).isEqualTo(overridden);
        }
    );
  }

  @Test
  void focus_oneSelectAll_oneSelectAt_oneSelect() {
    var overridden = "overridden";
    var lens = Lens.create(Library.class)
        .selectAll(Library::setBooks, Book.class)
        .selectAt(Book::setPages, 1, Page.class)
        .select(Page::setText, String.class);
    var target = new Library()
        .setBooks(List.of(
            new Book()
                .setTitle("Book 1")
                .setPages(List.of(
                    new Page().setText("This is some text"),
                    new Page().setText("Some more text")
                )),
            new Book()
                .setTitle("Book 2")
                .setPages(List.of(
                    new Page().setText(null),
                    new Page().setText("Text between some nulls"),
                    new Page().setText(null)
                )),
            new Book()
                .setTitle("Book 2")
                .setPages(List.of())
        ));

    assertThat(lens.path()).hasToString("$.books[*].pages[1].text");

    var result1 = lens.focus(target);
    assertThat(result1).hasSize(3);
    assertAll(
        () -> assertThat(result1.get(0).value()).isEqualTo("Some more text"),
        () -> assertThat(result1.get(0).path()).hasToString("$.books[0].pages[1].text"),
        () -> {
          result1.get(0).override(overridden);
          assertThat(target.getBooks().get(0).getPages().get(1).getText()).isEqualTo(overridden);
        },

        () -> assertThat(result1.get(1).value()).isEqualTo("Text between some nulls"),
        () -> assertThat(result1.get(1).path()).hasToString("$.books[1].pages[1].text"),
        () -> {
          result1.get(1).override(overridden);
          assertThat(target.getBooks().get(1).getPages().get(1).getText()).isEqualTo(overridden);
        },

        () -> assertThat(result1.get(2).value()).isEqualTo(null),
        () -> assertThat(result1.get(2).path()).hasToString("$.books[2].pages[1].text"),
        () -> {
          result1.get(2).override(overridden);
          assertThat(target.getBooks().get(2).getPages().get(1).getText()).isEqualTo(overridden);
        }
    );

    target.setBooks(null);
    var result2 = lens.focus(target);
    assertAll(
        () -> assertThat(result2).isEmpty()
    );
  }

  @Test
  void focus_oneSelect_twoSelectAll_oneSelect() {
    var overridden = "overridden";
    var lens = Lens.create(Library.class)
        .selectAll(Library::setBooks, Book.class)
        .selectAll(Book::setPages, Page.class)
        .select(Page::setText, String.class);
    var target = new Library()
        .setBooks(List.of(
            new Book()
                .setTitle("Book 1")
                .setPages(List.of(
                    new Page().setText("This is some text"),
                    new Page().setText("Some more text")
                )),
            new Book()
                .setTitle("Book 2")
                .setPages(List.of(
                    new Page().setText(null),
                    new Page().setText("Text between some nulls"),
                    new Page().setText(null)
                )),
            new Book()
                .setTitle("Book 2")
                .setPages(List.of())
        ));

    assertThat(lens.path()).hasToString("$.books[*].pages[*].text");

    var result1 = lens.focus(target);
    assertThat(result1).hasSize(5);
    assertAll(
        () -> assertThat(result1.get(0).value()).isEqualTo("This is some text"),
        () -> assertThat(result1.get(0).path()).hasToString("$.books[0].pages[0].text"),
        () -> {
          result1.get(0).override(overridden);
          assertThat(target.getBooks().get(0).getPages().get(0).getText()).isEqualTo(overridden);
        },

        () -> assertThat(result1.get(1).value()).isEqualTo("Some more text"),
        () -> assertThat(result1.get(1).path()).hasToString("$.books[0].pages[1].text"),
        () -> {
          result1.get(1).override(overridden);
          assertThat(target.getBooks().get(0).getPages().get(1).getText()).isEqualTo(overridden);
        },

        () -> assertThat(result1.get(2).value()).isNull(),
        () -> assertThat(result1.get(2).path()).hasToString("$.books[1].pages[0].text"),
        () -> {
          result1.get(2).override(overridden);
          assertThat(target.getBooks().get(1).getPages().get(0).getText()).isEqualTo(overridden);
        },

        () -> assertThat(result1.get(3).value()).isEqualTo("Text between some nulls"),
        () -> assertThat(result1.get(3).path()).hasToString("$.books[1].pages[1].text"),
        () -> {
          result1.get(3).override(overridden);
          assertThat(target.getBooks().get(1).getPages().get(1).getText()).isEqualTo(overridden);
        },

        () -> assertThat(result1.get(4).value()).isNull(),
        () -> assertThat(result1.get(4).path()).hasToString("$.books[1].pages[2].text"),
        () -> {
          result1.get(4).override(overridden);
          assertThat(target.getBooks().get(1).getPages().get(2).getText()).isEqualTo(overridden);
        }
    );

    target.setBooks(null);
    var result2 = lens.focus(target);
    assertAll(
        () -> assertThat(result2).isEmpty()
    );
  }

  @Test
  void overwrite_multiplePropertiesSameValue_shouldOverwriteCorrectOnes() {
    var sameValue = "sameValue";
    var lens = Lens.create(Library.class)
        .select(Library::setAddress, Address.class)
        .select(Address::setCity, String.class);
    var target = new Library()
        .setAddress(new Address()
            .setCity(sameValue)
            .setState(sameValue)
            .setZipcode(sameValue)
        );

    var result = lens.focus(target);

    assertThat(result.path()).hasToString("$.address.city");
    assertThat(result.value()).isEqualTo(sameValue);

    result.override("a new value");

    assertThat(target.getAddress().getCity()).isEqualTo("a new value");
    assertThat(target.getAddress().getState()).isEqualTo(sameValue);
    assertThat(target.getAddress().getZipcode()).isEqualTo(sameValue);
  }

  @Test
  void configure_fieldNameExtractor_shouldAffectPaths() {
    var lens = Lens
        .configureLensRoot(c -> c
            .fieldNameExtractor(p -> p.getAnnotation(CustomPath.class)
                .map(CustomPath::value)
                .orElse(p.name())
            )
        )
        .create(Library.class)
        .select(Library::setAddress, Address.class)
        .selectFirst(Address::setLines, Line.class)
        .configure(c -> c
            .fieldNameExtractor(FieldProperty::name)
        )
        .select(Line::setText, String.class);
    var target = new Library()
        .setAddress(new Address()
            .setLines(new ArrayList<>(List.of(
                new Line().setText("line1"),
                new Line().setText("line2")
            )))
        );

    var result = lens.focus(target);

    assertThat(result.value()).isEqualTo("line1");
    assertThat(result.path()).hasToString("$.myAddress.myLines[0].text");
  }
}