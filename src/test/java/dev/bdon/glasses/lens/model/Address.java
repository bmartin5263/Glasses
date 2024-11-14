package dev.bdon.glasses.lens.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Address {
  private String state;
  private String zipcode;
  @CustomPath("myLines")
  private List<Line> lines;
  private String city;

  public List<Line> getLines() {
    if (lines == null) {
      lines = new ArrayList<>();
    }
    return lines;
  }
}
