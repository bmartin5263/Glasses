package dev.bdon.glasses.util;

import java.util.List;

public class Default {
  private Default() {}

  @SuppressWarnings("unchecked")
  public static <E, T extends List<E>> T toEmpty(T list) {
    if (list != null) {
      return (T) List.copyOf(list);
    }
    return (T) List.<E>of();
  }
}
