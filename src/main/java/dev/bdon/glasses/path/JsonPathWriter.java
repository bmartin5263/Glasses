package dev.bdon.glasses.path;

import java.util.regex.Pattern;

public class JsonPathWriter implements PathWriter {
  private boolean writeRootNode = true;
  private Pattern specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9]");

  public JsonPathWriter() {
  }

  public JsonPathWriter(boolean writeRootNode, Pattern specialCharacterPattern) {
    this.writeRootNode = writeRootNode;
    this.specialCharacterPattern = specialCharacterPattern;
  }

  @Override
  public void write(FieldNode node, StringBuilder path) {
    var name = node.name();
    if (containsSpecialCharacters(name)) {
      path.append("['").append(name).append("']");
    }
    else {
      path.append('.').append(name);
    }
  }

  @Override
  public void write(IndexFilterNode node, StringBuilder path) {
    path.append('[').append(node.expression()).append(']');
  }

  @Override
  public void write(IndexNode node, StringBuilder path) {
    path.append('[').append(node.index()).append(']');
  }

  @Override
  public void writeRoot(StringBuilder path) {
    if (writeRootNode) {
      path.append("$");
    }
  }

  private boolean containsSpecialCharacters(String text) {
    return specialCharacterPattern.matcher(text).find();
  }
}
