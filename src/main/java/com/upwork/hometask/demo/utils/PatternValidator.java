package com.upwork.hometask.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternValidator {
    public static final String IP_PATTERN_MESSAGE = "Ip is not valid";
    public static final String IP_PATTERN = "(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";

    private static final Pattern ipPattern = Pattern.compile(IP_PATTERN);

    public static boolean isValidIPAddress(final String ip) {
        Matcher matcher = ipPattern.matcher(ip);
        return matcher.matches();
    }

    public static void main(String[] args) {

        String str1 = "000.12.12.034";
        System.out.println("Input: " + str1);
        System.out.println(
                "Output: "
                        + isValidIPAddress(str1));

        // Test Case: 2
        System.out.println("\nTest Case 2:");
        String str2 = "121.234.12.12";
        System.out.println("Input: " + str2);
        System.out.println(
                "Output: "
                        + isValidIPAddress(str2));

        // Checking for False case.
        // Test Case: 3
        System.out.println("\nTest Case 3:");
        String str3 = "000.12.234.23.23";
        System.out.println("Input: " + str3);
        System.out.println(
                "Output: "
                        + isValidIPAddress(str3));

        // Test Case: 4
        System.out.println("\nTest Case 4:");
        String str4 = "I.Am.not.an.ip";
        System.out.println("Input: " + str4);
        System.out.println(
                "Output: "
                        + isValidIPAddress(str4));

    }

}