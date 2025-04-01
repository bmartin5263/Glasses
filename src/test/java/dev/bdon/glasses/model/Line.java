package dev.bdon.glasses.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Line {
  @CustomPath("myText")
  String text;
}
