package dev.bdon.glasses.lens.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Employee {
  private String name;
  private BigDecimal salary;
}
