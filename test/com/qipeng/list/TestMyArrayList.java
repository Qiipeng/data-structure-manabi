package com.qipeng.list;

import com.qipeng.linearList.arrayList.MyArrayList;


public class TestMyArrayList {

    public static void main(String[] args) {
        MyArrayList<Integer> list = new MyArrayList<>();
        list.add(11);
        list.add(22);
        list.add(33);
        list.add(44);
        list.add(55);
        list.add(3, 99);
        list.add(88);

        list.set(2, 100);

        System.out.println(list.size());
        System.out.println(list);
    }
}
