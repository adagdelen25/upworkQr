package com.upwork.hometask.demo.utils;

import com.upwork.hometask.demo.models.exception.InvalidIPAddressException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {
    /*
    this method convert ip string long value to search numeric values
     */
    public static Long ipToLong(String ip) {
        try {
            byte[] octets = InetAddress.getByName(ip).getAddress();
            long result = 0;
            for (byte octet : octets) {
                result <<= 8;
                result |= octet & 0xff;
            }
            return result;
        } catch (UnknownHostException e) {
            throw new InvalidIPAddressException(e.getMessage());
        }
    }
}
