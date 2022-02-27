package com.upwork.hometask.demo.rangetree;

@Deprecated
public final class Range<T extends Comparable<T>> {
    private final T lowerInclusive;
    private final T upperInclusive;
    private final T lowerInclusive1;
    private final T upperInclusive1;
    public Range(T lowerInclusive, T upperInclusive,T lowerInclusive1, T upperInclusive1) {
        this.lowerInclusive = lowerInclusive;
        this.upperInclusive = upperInclusive;
        this.lowerInclusive1 = lowerInclusive1;
        this.upperInclusive1 = upperInclusive1;
    }
    public boolean contains(T value,T value2) {
        return (value.compareTo(this.lowerInclusive) >= 0 &&
                value.compareTo(this.upperInclusive) <= 0)
            &&(value2.compareTo(this.lowerInclusive1) >= 0 &&
            value2.compareTo(this.upperInclusive1) <= 0);
    }
    @Override
    public String toString() {
        return "(" + this.lowerInclusive + "-" + this.upperInclusive + ")" +"(" + this.lowerInclusive1 + "-" + this.upperInclusive1 + ")";
    }
}