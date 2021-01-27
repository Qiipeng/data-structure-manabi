package com.qipeng.set;


import com.qipeng.set.impl.MyListSet;
import com.qipeng.set.impl.MyTreeSet;

public class TestMySet {

    public static void main(String[] args) {
        test2();
    }

    // linkedList实现的Set
    public static void test() {
        MySet<Integer> listSet = new MyListSet<>();
        listSet.add(10);
        listSet.add(20);
        listSet.add(30);
        listSet.add(40);

        listSet.traversal(new MySet.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.println(element);
                return false;
            }
        });
    }

    // RBTree实现的Set
    public static void test2() {
        MySet<Integer> treeSet = new MyTreeSet<>();
        treeSet.add(10);
        treeSet.add(20);
        treeSet.add(30);
        treeSet.add(40);
        treeSet.add(50);
        treeSet.add(10);

        treeSet.traversal(new MySet.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.println(element);
                return false;
            }
        });
    }
}
