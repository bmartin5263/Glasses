package dev.bdon.lens.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Address {
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
