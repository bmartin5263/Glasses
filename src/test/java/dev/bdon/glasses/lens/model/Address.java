package dev.bdon.glasses.lens.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Address {
  @CustomPath("myPath")
  private String city;
  private String state;
  private String zipcode;
  private List<Line> lines;

  public List<Line> getLines() {
    if (lines == null) {
      lines = new ArrayList<>();
    }
    return lines;
  }
}
