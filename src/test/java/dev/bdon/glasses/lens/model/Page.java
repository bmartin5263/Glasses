package dev.bdon.glasses.lens.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Page {
  private String text;
}
