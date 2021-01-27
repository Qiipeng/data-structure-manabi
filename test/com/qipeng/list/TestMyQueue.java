package com.qipeng.list;

import com.qipeng.linearList.queue.MyCirlcleDeque;


public class TestMyQueue {

    public static void main(String[] args) {
        testMyPriorityQueue();
    }

    private static void testQueue() {
        MyCirlcleDeque<Integer> deque = new MyCirlcleDeque<>();

        // 6 5 4 3 2 1 100 101 102 103 105 105 106 8 7 6
        // 8 7 6 5 4 3 2 1 100 101 102 103 104 105 106 107 108 109 null null 10 9
        for (int i = 0; i < 10; i++) {
            deque.enQueueFront(i + 1);
            deque.enQueueRear(i + 100);
        }

        System.out.println(deque);

        deque.deQueueFront();
        deque.deQueueRear();

        System.out.println(deque);
    }

    private static void testMyPriorityQueue() {

    }
}
