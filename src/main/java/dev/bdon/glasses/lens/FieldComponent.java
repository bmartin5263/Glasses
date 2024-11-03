package dev.bdon.glasses.lens;

public class FieldComponent implements PathComponent {
  private final String name;

  public FieldComponent(String name) {
    this.name = "." + name;
  }

  @Override
  public String toString() {
    return name;
  }
}
