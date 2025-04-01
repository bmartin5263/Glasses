package dev.bdon.glasses.type;

public class PrimitiveFieldClass {
  private int someField;

  public PrimitiveFieldClass() {
  }

  public PrimitiveFieldClass(int someField) {
    this.someField = someField;
  }

  public int someField() {
    return someField;
  }

  public void setSomeField(int someField) {
    this.someField = someField;
  }
}