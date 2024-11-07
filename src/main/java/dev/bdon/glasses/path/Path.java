package dev.bdon.glasses.path;

import java.util.*;
import java.util.stream.Collectors;

public class Path implements Iterable<PathNode> {
  private static final JsonPathWriter DEFAULT_PATH_WRITER = new JsonPathWriter();
  private final List<PathNode> nodes;

  public Path() {
    this(List.of());
  }

  public Path(List<PathNode> nodes) {
    this.nodes = new ArrayList<>(nodes);
  }

  @Override
  public String toString() {
    return toString(DEFAULT_PATH_WRITER);
  }

  public String toString(PathWriter pathWriter) {
    return pathWriter.write(this);
  }

  public Path append(PathNode node) {
    nodes.add(node);
    return this;
  }

  public Deque<DynamicNode> dynamicNodeStack() {
    return nodes.stream()
        .filter(c -> DynamicNode.class.isAssignableFrom(c.getClass()))
        .map(DynamicNode.class::cast)
        .collect(Collectors.toCollection(ArrayDeque::new));
  }

  public Path copy() {
    return new Path(nodes);
  }

  @Override
  public Iterator<PathNode> iterator() {
    return nodes.iterator();
  }
}
