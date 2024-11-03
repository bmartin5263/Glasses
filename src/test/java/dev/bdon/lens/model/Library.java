package dev.bdon.lens.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Library {
  private Address address;
  private List<Media> media;
  private List<Book> books;
  private Map<DepartmentName, Department> departments;
}
