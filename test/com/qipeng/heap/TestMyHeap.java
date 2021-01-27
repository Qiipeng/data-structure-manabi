package com.qipeng.heap;

import com.qipeng.heap.impl.MyBinaryHeap;
import com.qipeng.utils.binaryTreePrinter.printer.BinaryTrees;

public class TestMyHeap {

    public static void main(String[] args) {
        testMyBinaryHeap2();
    }

    private static void testMyBinaryHeap() {
        MyBinaryHeap<Integer> heap = new MyBinaryHeap<>();
        heap.add(72);
        heap.add(43);
        heap.add(38);
        heap.add(50);
        heap.add(68);
        heap.add(10);
        heap.add(90);

        BinaryTrees.println(heap);

        heap.remove();

        BinaryTrees.println(heap);

        heap.replace(100);

        BinaryTrees.println(heap);
    }

    private static void testMyBinaryHeap1() {
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        MyBinaryHeap<Integer> heap = new MyBinaryHeap<>(data);

        BinaryTrees.println(heap);
    }

    private static void testMyBinaryHeap2() {
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        // 最小堆
        MyBinaryHeap<Integer> heap = new MyBinaryHeap<>(data, (o1, o2) -> o2 - o1);

        BinaryTrees.println(heap);
    }

}
