package dev.bdon.glasses.lens;

import dev.bdon.glasses.lens.element.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class LensUtils {
  private LensUtils() {}

  // Reverses the element linked list
  static <A, B> Stream<Element<A, B>> reverseElementChain(Element<A, B> last) {
    var elements = new ArrayList<Element<A, B>>();
    for (var current = last; current != null; current = current.parent()) {
      elements.add(current);
    }
    Collections.reverse(elements);
    return elements.stream();
  }

}
