package dev.bdon.glasses.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Book extends Media {
  private String title;
  private String author;
  private BookContent content;
  private List<Page> pages;
}
