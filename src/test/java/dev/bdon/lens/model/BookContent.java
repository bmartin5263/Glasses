package dev.bdon.lens.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BookContent {
  private List<Page> pages;
}
