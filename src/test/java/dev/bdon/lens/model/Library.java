package dev.bdon.lens.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Library {
  private Address address;
  private List<Media> media;
  private List<Department> departments;
}
