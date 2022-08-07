package com.upwork.hometask.demo.bineary;

import lombok.Data;

@Data
class Node {
    int start;
    int end;
    Node left;
    Node right;

    Node(int start,int end) {
        this.start = start;
        this.end = end;
        right = null;
        left = null;
    }

    public String getValue(){
        return start +" :: "+ end;
    }

}