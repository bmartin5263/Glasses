package dev.bdon.glasses.util;

import java.util.List;

/**
 * Utility class for null-safe helper functions
 */
public class NullSafe {

  /**
   * No instances
   */
  private NullSafe() {}

  /**
   * Copies a list, nulls result in an empty list
   *
   * @param list the nullable list
   * @param <E> the list element type
   * @param <T> the list implementation type
   *
   * @return a copy of the list if not null, else an empty list
   */
  @SuppressWarnings("unchecked")
  public static <E, T extends List<E>> T copy(T list) {
    if (list != null) {
      return (T) List.copyOf(list);
    }
    return (T) List.<E>of();
  }
}
