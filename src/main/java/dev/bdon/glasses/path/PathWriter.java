package dev.bdon.glasses.path;

public interface PathWriter {
  void writeRoot(StringBuilder path);
  void write(FieldNode node, StringBuilder path);
  void write(IndexFilterNode node, StringBuilder path);
  void write(IndexNode node, StringBuilder path);

  default String write(Path path) {
    var sb = new StringBuilder();
    writeRoot(sb);
    for (var node : path) {
      node.accept(this, sb);
    }
    return sb.toString();
  }
}
