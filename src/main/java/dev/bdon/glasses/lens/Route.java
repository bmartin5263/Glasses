package dev.bdon.glasses.lens;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Route {
  private final List<PathComponent> components;

  public Route() {
    this.components = List.of();
  }

  public Route(List<PathComponent> components) {
    this.components = List.copyOf(components);
  }

  public static Route start() {
    return new Route();
  }

  public String path() {
    if (components.isEmpty()) {
      return "$";
    }
    return "$" + components.stream().map(PathComponent::toString).collect(Collectors.joining());
  }

  public Route addFieldComponent(String name) {
    return new Route(Stream.concat(
        components.stream(),
        Stream.of(new FieldComponent(name))
    ).toList());
  }

  public Route addIndexComponent(int index) {
    return new Route(Stream.concat(
        components.stream(),
        Stream.of(new IndexComponent(index))
    ).toList());
  }

  public Deque<DynamicComponent> stack() {
    return components.stream()
        .filter(c -> DynamicComponent.class.isAssignableFrom(c.getClass()))
        .map(DynamicComponent.class::cast)
        .collect(Collectors.toCollection(ArrayDeque::new));
  }
}
