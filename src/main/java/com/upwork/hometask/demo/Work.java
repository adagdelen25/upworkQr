package com.upwork.hometask.demo;

import java.util.HashSet;
import java.util.Set;

public class Work {

  public static void main(String[] args) {
    System.out.println(solution("ba1","1Ad"));
  }

  public static boolean solution(String S, String T) {
    String a = "";
    for (int i = 0; i < S.length(); i++) {
      if(Character.isDigit(S.charAt(i))){
        for (int i1 = 0; i1 < Integer.parseInt(S.charAt(i)+""); i1++) {
          a+="-";
        }
      }else{
        a+=S.charAt(i);
      }
    }
    String b = "";
    for (int i = 0; i < T.length(); i++) {
      if(Character.isDigit(T.charAt(i))){
        for (int i1 = 0; i1 < Integer.parseInt(T.charAt(i)+""); i1++) {
          b+="-";
        }
      }else{
        b+=T.charAt(i);
      }
    }

    if(a.length() != b.length())
      return false;

    for (int i = 0; i < a.length(); i++) {
      if(a.charAt(i) == '-')
        continue;
      if(b.charAt(i) == '-')
        continue;
      if(b.charAt(i) == a.charAt(i))
        continue;
      return false;
    }

    return true;
  }

}
