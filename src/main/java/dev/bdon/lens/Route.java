package dev.bdon.lens;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Route {
  private final List<String> components;

  public Route() {
    this.components = List.of();
  }

  public Route(List<String> components) {
    this.components = List.copyOf(components);
  }

  public static Route start() {
    return new Route();
  }

  public String path() {
    if (components.isEmpty()) {
      return "$";
    }
    return "$" + String.join("", components);
  }

  public Route addComponent(String pathComponent) {
    return new Route(Stream.concat(
        components.stream(),
        Stream.of(pathComponent)
    ).toList());
  }
}
