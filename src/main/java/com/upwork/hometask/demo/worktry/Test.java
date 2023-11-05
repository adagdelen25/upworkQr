package com.upwork.hometask.demo.worktry;

import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test {


    public static void test2() {
        RangeMap<Integer, String> experienceRangeDesignationMap
                = TreeRangeMap.create();
        experienceRangeDesignationMap.put(
                Range.closed(0, 2), "Associate");
        experienceRangeDesignationMap.put(
                Range.closed(3, 5), "Senior Associate");
        experienceRangeDesignationMap.put(
                Range.closed(6, 8), "Vice President");
        experienceRangeDesignationMap.put(
                Range.closed(9, 15), "Executive Director");
        String s = experienceRangeDesignationMap.get(6);
        System.out.println(s);
    }


    public static void test1() {
        RangeMap<Long, RangeMap<Long, Boolean>> sourceRange = TreeRangeMap.create();
        RangeMap<Long, Boolean> rangeMap = TreeRangeMap.create();
        Range<Long> closed2 = Range.closed(0L, 10L);
        rangeMap.put(closed2, true);

        Range<Long> closed3 = Range.closed(5L, 8L);
        rangeMap.put(closed3, false);

        Range<Long> closed1 = Range.closed(0L, 10L);
        sourceRange.put(closed1, rangeMap);

        Boolean aBoolean = sourceRange.get(1L).get(6L);
        System.out.println(aBoolean);

        rangeMap.remove(closed3);

        aBoolean = sourceRange.get(1L).get(6L);
        System.out.println(aBoolean);

    }

    public static void test4() {
        RangeMap<Integer, Integer> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closed(3, 7), 1);
        rangeMap.put(Range.closed(9, 10), 2);
        rangeMap.put(Range.closed(12, 16), 3);

        RangeMap<Integer, Integer> subRangeMap = rangeMap.subRangeMap(Range.closed(3, 16));
        subRangeMap.asMapOfRanges().forEach(
                (integerRange, integer) -> {
                    System.out.println(integerRange);
                }
        );


        System.out.println(rangeMap.get(3));

        //10.10.10.10   //20.20.20.20 allow
        //15.15.15.15   //25.25.25.25 deny


    }


    public static void main(String[] args) {
        test4();
    }

    public static void test3() {

        final RangeSet<Long> characterRanges = TreeRangeSet.create();

        Range<Long> range1 = Range.closedOpen(1L, 10L);
        Range<Long> range2 = Range.closedOpen(2L, 5L);
        Range<Long> range3 = Range.closedOpen(11L, 20L);


        characterRanges.add(range1);
        characterRanges.add(range2);
        characterRanges.add(range3);

        Range<Long> longRange = characterRanges.rangeContaining(3L);

        System.out.println(longRange.lowerEndpoint());
        System.out.println(longRange.upperEndpoint());


    }

    public List<String> getIntersecting(String contig, int start, int end) {
        List<String> nameList = new ArrayList<>();
        Range<Integer> range = Range.closed(start, end);

        // get the contig-specific list of range maps
        List<TreeRangeMap<Integer, String>> contigRanges = null;

        if (contigRanges == null) {
            return nameList;
        }/*from   w  w  w.  java2 s .co  m*/

        // check each map in the list for intersection
        for (int i = 0; i < contigRanges.size(); i++) {
            TreeRangeMap<Integer, String> map = contigRanges.get(i);
            Map<Range<Integer>, String> intersecting = map.subRangeMap(range).asMapOfRanges();

            // add all intersecting ranges from the current map to the return
            // list
            for (Range<Integer> key : intersecting.keySet()) {
                nameList.add(intersecting.get(key));
            }
        }

        return nameList;
    }

    private void add(List<TreeRangeMap<Integer, String>> contigRanges, Range<Integer> range, String name) {
        TreeRangeMap<Integer, String> map = null;

        // find the first tree range map that has no range conflicting
        // with the given range
        // this is because Guava range maps don't accommodate intersecting
        // ranges/* ww  w .  j av  a 2 s  .  c om*/
        int index = 0;
        for (; index < contigRanges.size(); index++) {
            map = contigRanges.get(index);
            if (map.subRangeMap(range).asMapOfRanges().size() == 0) {
                break;
            }
        }

        // did not break, requires adding new map
        if (index == contigRanges.size()) {
            map = TreeRangeMap.create();
            contigRanges.add(map);
        }

        // add the current range to the selected map
        map.put(range, name);
    }


}
