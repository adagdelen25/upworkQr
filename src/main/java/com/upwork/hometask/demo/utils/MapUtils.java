package com.upwork.hometask.demo.utils;

import java.math.BigDecimal;
import java.math.MathContext;

public class MapUtils {
    public static Double duration(Double distance) {
        MathContext m = new MathContext(2); // 4 precision
        BigDecimal b1 = new BigDecimal((distance * 60) / 40);
        BigDecimal b2 = b1.round(m);
        return b2.doubleValue();
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            }
            if (unit.equals("M")) {
                dist = dist * 1.609344 * 1000;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}
