package dev.bdon.glasses.type;

import dev.bdon.glasses.util.LensConfigurationException;
import dev.bdon.glasses.util.LensConventionViolatedException;
import dev.bdon.glasses.util.LensInternalException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TypeTest {

  @Test
  void findProperty_withTwoFieldsHavingSameValue_shouldSelectCorrectOneBasedOnSetter() {
    var someClassType = Type.from(SomeClass.class);
    var instance = new SomeClass("", "");

    var property = someClassType.findProperty(instance, SomeClass::setAnotherField, String.class);
    assertThat(property.name()).isEqualTo("anotherField");

    property = someClassType.findProperty(instance, SomeClass::setSomeField, String.class);
    assertThat(property.name()).isEqualTo("someField");
  }

  @Test
  void findProperty_withInvalidSetter_shouldThrow() {
    var someClassType = Type.from(SomeClass.class);
    var instance = new SomeClass("hello", "world");

    assertThatExceptionOfType(LensConfigurationException.class)
        .isThrownBy(() -> someClassType.findProperty(instance, (target, tracer) -> { /* set nothing! */}, Integer.class))
        .withMessageContaining("Unable to find property of type class java.lang.Integer on target type class dev.bdon.glasses.type.SomeClass");
  }

  @Test
  void findProperty_withNonDefaultConstructableCustomType_shouldThrow() {
    var someClassType = Type.from(Wrapper.class);
    var instance = new Wrapper();

    assertThatExceptionOfType(LensConventionViolatedException.class)
        .isThrownBy(() -> someClassType.findProperty(instance, Wrapper::setNonDefaultConstructableType, NonDefaultConstructableType.class))
        .withMessageContaining("Class 'class dev.bdon.glasses.type.TypeTest$NonDefaultConstructableType' does not have a default constructor available");
  }

  // Purely for test coverage, in practice this shouldn't happen
  @Test
  @SuppressWarnings("unchecked")
  void findProperty_withCorruptedTracerFactory_shouldFail() throws NoSuchFieldException, IllegalAccessException {
    // Corrupt the Tracer Factory to stop producing unique instances
    var string = "the same string";
    var field = Tracers.class.getDeclaredField("TRACER_FACTORY_BY_TYPE");
    field.setAccessible(true);
    var value = (Map<Class<?>, Supplier<?>>) field.get(null);
    value.put(String.class, () -> string);

    var someClassType = Type.from(SomeClass.class);
    var instance = new SomeClass(string, string);

    assertThatExceptionOfType(LensInternalException.class)
        .isThrownBy(() -> someClassType.findProperty(instance, SomeClass::setAnotherField, String.class))
        .withMessageContaining("Ambiguous property")
        .withMessageContaining("match for type class java.lang.String")
        .withMessageContaining("FieldProperty[javaField=JavaField[class=class dev.bdon.glasses.type.SomeClass, name=anotherField, type=class java.lang.String], genericArguments=[]]")
        .withMessageContaining("FieldProperty[javaField=JavaField[class=class dev.bdon.glasses.type.SomeClass, name=someField, type=class java.lang.String], genericArguments=[]]");

    assertThatExceptionOfType(LensInternalException.class)
        .isThrownBy(() -> someClassType.findProperty(instance, SomeClass::setSomeField, String.class))
        .withMessageContaining("Ambiguous property")
        .withMessageContaining("match for type class java.lang.String")
        .withMessageContaining("FieldProperty[javaField=JavaField[class=class dev.bdon.glasses.type.SomeClass, name=anotherField, type=class java.lang.String], genericArguments=[]]")
        .withMessageContaining("FieldProperty[javaField=JavaField[class=class dev.bdon.glasses.type.SomeClass, name=someField, type=class java.lang.String], genericArguments=[]]");

  }

  @SuppressWarnings("FieldCanBeLocal")
  private static class Wrapper {
    private NonDefaultConstructableType nonDefaultConstructableType;

    public void setNonDefaultConstructableType(NonDefaultConstructableType nonDefaultConstructableType) {
      this.nonDefaultConstructableType = nonDefaultConstructableType;
    }
  }

  private static class NonDefaultConstructableType {
    public NonDefaultConstructableType(String something) {

    }
  }
}