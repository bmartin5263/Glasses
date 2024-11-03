package dev.bdon.glasses.lens;

public class IndexComponent implements DynamicComponent {
  private final int index;

  public IndexComponent(int index) {
    this.index = index;
  }

  public int index() {
    return index;
  }

  @Override
  public String toString() {
    return "[%s]".formatted(index);
  }
}
