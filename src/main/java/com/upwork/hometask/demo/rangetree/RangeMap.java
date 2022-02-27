package com.upwork.hometask.demo.rangetree;

import java.util.Map;
import java.util.TreeMap;

/*
it is not supported four leaves and return the first record he founded but sometime we need next record
 */
@Deprecated
public final class RangeMap<T extends Comparable<T>> {
  private TreeMap<T, Range<T>> map = new TreeMap<>();

  public void add(Range<T> range,T key) {
    this.map.put(key, range);
  }

  public boolean contains(T value,T value2) {
    Map.Entry<T, Range<T>> tRangeEntry = this.map.floorEntry(value);
    this.map.tailMap(value,false).forEach((t, tRange) -> System.out.println(tRange));
    return true;
  }


  public static void main(String[] args) {
    RangeMap<Long> map = new RangeMap<>();
      map.add(new Range<>(2L, 2L,3L, 4L),1L);
      map.add(new Range<>(1L, 3L,3L, 4L),2L);
      map.add(new Range<>(1L, 4L,4L, 4L),3L);
      map.add(new Range<>(4L, 4L,4L, 4L),3L);
      map.add(new Range<>(5L, 4L,4L, 4L),3L);
      map.add(new Range<>(0L, 4L,6L, 4L),3L);
    System.out.println("192.168.1.3" + ": " + map.contains(1L,3L));



  }
}
