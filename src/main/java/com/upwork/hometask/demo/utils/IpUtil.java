package com.upwork.hometask.demo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {

  /*
  this method convert ip string long value to search numeric values
   */
  public static Long ipToLong(String ip) throws UnknownHostException {
    byte[] octets = InetAddress.getByName(ip).getAddress();
    long result = 0;
    for (byte octet : octets) {
      result <<= 8;
      result |= octet & 0xff;
    }
    return result;
  }

  public static void main(String[] args) {
    // 3234332672
    try {
      System.out.println(ipToLong("255.255.255.255"));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }
}
