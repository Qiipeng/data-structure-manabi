package com.qipeng.linearList.queue;

import com.qipeng.heap.MyHeap;
import com.qipeng.heap.impl.MyBinaryHeap;

import java.util.Comparator;

/**
 * 优先级队列二叉堆实现
 */
public class MyPriorityQueue<E> {

    private MyHeap<E> heap;
    private int size;

    public MyPriorityQueue(Comparator<E> comparator) {
        heap = new MyBinaryHeap<>(comparator);
    }

    public MyPriorityQueue() {
        this(null);
    }

    /**
     * 元素数量
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * 入队
     *
     * @param element
     */
    public void enQueue(E element) {
        heap.add(element);
    }

    /**
     * 出队
     *
     * @return
     */
    public E deQueue() {
        return heap.remove();
    }

    /**
     * 获取队列的头元素
     *
     * @return
     */
    public E front() {
        return heap.get();
    }

    /**
     * 清空队列
     */
    public void clear() {
        heap.clear();
    }
}
