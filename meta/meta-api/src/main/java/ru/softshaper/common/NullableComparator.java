package ru.softshaper.common;

import java.util.Comparator;

/**
 * Created by Sunchise on 28.02.2017.
 */
public class NullableComparator<T extends Comparable> implements Comparator<T> {

  @Override
  public int compare(T o1, T o2) {
    if (o1 == null) {
      if (o2 == null) {
        return 0;
      } else {
        return -1;
      }
    }
    if (o2 == null) {
      return 1;
    }
    return o1.compareTo(o2);
  }

}
