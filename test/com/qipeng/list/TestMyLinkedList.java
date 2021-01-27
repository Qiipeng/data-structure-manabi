package com.qipeng.list;

import com.qipeng.linearList.MyList;
import com.qipeng.linearList.linkedList.MyCircleLinkedList;

public class TestMyLinkedList {

    public static void main(String[] args) {
        MyList<Integer> list = new MyCircleLinkedList<>();
        list.add(11);
        list.add(22);
        list.add(33);
        list.add(44);
        list.add(55);
        list.add(0, 99);

        list.set(2, 88);

        System.out.println(list);

        System.out.println(list.remove(2));

        System.out.println(list);

        josephus();
    }

    private static void josephus() {
        MyCircleLinkedList<Integer> list = new MyCircleLinkedList<>();

        for (int i = 1; i <= 8; i++) {
            list.add(i);
        }

        list.reset(); // 指向 1

        while (!list.isEmpty()) {
            list.next();
            list.next();
            System.out.println(list.remove());
        }
    }
}
