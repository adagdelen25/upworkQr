package com.upwork.hometask.demo.bineary;

import lombok.Data;

@Data
public class BinaryTree {
    Node root;
    public void add(Node node){
        if(root == null){
            root = node;
        }
        if(node.getStart()< root.getStart()){
            if(root.getLeft() == null){
                root.setLeft(node);
            }else{
                add(node,root.getLeft());
            }
        }
        else if(node.getEnd()> root.getEnd()){
            if(root.getRight() == null){
                root.setRight(node);
            }else{
                add(node,root.getRight());
            }
        }
    }

    private String find(int i){
        if(i< root.getEnd() && i> root.getStart()){
            return root.getValue();
        }
        if(root.getLeft() != null && i< root.getStart()){
           return find(i,root.getLeft());
        }
        if(root.getRight() != null && i> root.getEnd()){
            return find(i,root.getRight());
        }
        return null;
    }

    private String find(int i,Node root){
        if(i< root.getEnd() && i> root.getStart()){
            return root.getValue();
        }
        if(root.getLeft() != null && i< root.getStart()){
            return find(i,root.getLeft());
        }
        if(root.getRight() != null && i> root.getEnd()){
            return find(i,root.getRight());
        }
        return null;
    }

    public void add(Node node, Node parent){
        if(node.getStart()< parent.getStart()){
            if(parent.getLeft() == null){
                parent.setLeft(node);
            }else{
                add(node,parent.getLeft());
            }
        }
        else if(node.getEnd()> parent.getEnd()){
            if(parent.getRight() == null){
                parent.setRight(node);
            }else{
                add(node,parent.getRight());
            }
        }
    }

    public  void print(Node node){

        if(node == null)
            return;
        if (node.getLeft() != null) {
           print(node.getLeft());
        }
        System.out.println(node.getStart() +" :: "+node.getEnd());
        if (node.getRight() != null) {
            print(node.getRight());
        }

    }

    public static void main(String[] args) {


       /* BinaryTree binaryTree = new BinaryTree();
        Node node1 = new Node(30,40);
        binaryTree.add(node1);
        Node node2 = new Node(1,10);
        binaryTree.add(node2);
        Node node3 = new Node(60,70);
        binaryTree.add(node3);
        Node node4 = new Node(11,20);
        binaryTree.add(node4);
        binaryTree.print(binaryTree.root);

        System.out.println(binaryTree.find(5));
        System.out.println(binaryTree.find(65));*/

        System.out.println("123456".substring(0,3));

    }


}