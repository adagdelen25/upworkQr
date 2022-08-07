package com.upwork.hometask.demo;

public class Work3 {

  public static void main(String[] args) {
    System.out.println(test2());

  }

  public static  int test() {
    String S  ="";
    int length = S.length();
    for (int i =length-1 ; i >=0 ; i--) {
      if(S.substring(0,i).equals(S.substring(length-i)) ){
       return i;
      }
    }

    return 0;
  }

  public static  int test2() {
    String S  ="tetet";

    // a,b,b,a,b,b,a

    // a,b,b,a,b,b
    // b,b,a,b,b,a


    // a,b,b,a,b
    // b,a,b,b,a

    // a,b,b,a
    // a,b,b,a

    char[] chars = S.toCharArray();
    int length = S.length();
    for (int i =length-1 ; i >=0 ; i--) {
       int a = length-i;
       stop:
       {
         for (int i1 = 0; i1 < i; i1++) {
           if (chars[i1] != chars[a--]) {
             break stop;
           }
         }
         return i;
       }
    }

    return 0;
  }


}
