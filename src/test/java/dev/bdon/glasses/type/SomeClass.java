package dev.bdon.glasses.type;

public class SomeClass {
  private String someField;
  private String anotherField;

  public SomeClass() {
  }

  public SomeClass(String someField) {
    this.someField = someField;
  }

  public SomeClass(String someField, String anotherField) {
    this.someField = someField;
    this.anotherField = anotherField;
  }

  public String getSomeField() {
    return someField;
  }

  public String getAnotherField() {
    return anotherField;
  }

  public void setSomeField(String someField) {
    this.someField = someField;
  }

  public void setAnotherField(String anotherField) {
    this.anotherField = anotherField;
  }
}