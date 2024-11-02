package dev.bdon.lens.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Line {
  String text;
}
