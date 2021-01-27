package com.qipeng.tree;

import com.qipeng.tree.avlTree.MyAVLTree;
import com.qipeng.tree.binaryTree.MyBST;
import com.qipeng.tree.redBlackTree.MyRBTree;
import com.qipeng.utils.binaryTreePrinter.printer.BinaryTrees;


public class TestMyBinarySearchTree {

    public static void main(String[] args) {
        test3();
    }

    private static void test3() {
        // 55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50
        Integer[] data = new Integer[]{55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50};
        MyBST<Integer> bst = new MyBST<>();

        for (Integer datum : data) {
            bst.add(datum);
        }

        BinaryTrees.println(bst);

        bst.postorder1(new com.qipeng.com.qipeng.tree.binaryTree.MyBinaryTree.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.println(element);

                return false;
            }
        });
    }


    // RedBlack-remove
    private static void test2() {
        // 55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50
        Integer[] data = new Integer[]{55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50};
        MyRBTree<Integer> rbTree = new MyRBTree<>();

        for (Integer datum : data) {
            rbTree.add(datum);
        }

        BinaryTrees.println(rbTree);

        for (Integer datum : data) {
            rbTree.remove(datum);
            System.out.println("----------------");
            System.out.println("【" + datum + "】");
            BinaryTrees.println(rbTree);
        }
    }

    // RedBlack
    private static void test1() {
        // 55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50
        Integer[] data = new Integer[]{55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50};
        MyRBTree<Integer> rbTree = new MyRBTree<>();

        for (Integer datum : data) {
            rbTree.add(datum);
        }

        BinaryTrees.println(rbTree);
    }

    // AVL
    private static void test() {
        // 85, 19, 69, 3, 7, 99, 95, 2, 1, 70, 44, 58, 11, 21, 14, 93, 57, 4, 56
        Integer[] data = new Integer[]{85, 19, 69, 3, 7, 99, 95};
        // MyBST<Integer> bst = new MyBST<>();
        MyAVLTree<Integer> avl = new MyAVLTree<>();

        for (Integer datum : data) {
            avl.add(datum);
        }

        avl.remove(99);
        avl.remove(85);
        avl.remove(95);
        BinaryTrees.println(avl);
    }
}
