package dev.bdon.glasses.path;

public interface PathNode {
  void accept(PathWriter pathWriter, StringBuilder writer);
}
