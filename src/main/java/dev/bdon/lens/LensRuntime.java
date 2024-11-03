package dev.bdon.lens;

public class LensRuntime {
  private final Lens<?, ?> lens;

  public LensRuntime(Lens<?, ?> lens) {
    this.lens = lens;
  }

  public LensContext context() {
    return lens.context();
  }

  public Lens<?, ?> lens() {
    return lens;
  }
}
