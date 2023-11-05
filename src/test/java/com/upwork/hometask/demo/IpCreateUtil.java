package com.upwork.hometask.demo;

public class IpCreateUtil {
    public static String getRandomIpAddress(String start) {
        return start + "." + getRandomInt(0, 255) + "."
                + getRandomInt(0, 255) + "." + getRandomInt(0, 255);
    }/*from   ww  w  . j  av a2  s . co  m*/

    public static int getRandomInt() {
        return getRandomInt(1, Integer.MAX_VALUE);
    }

    public static int getRandomInt(int min, int max) {
        return min + (int) ((Math.random() * (max - min)));
    }
}
