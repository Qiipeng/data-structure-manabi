package com.qipeng.heap.impl;

import com.qipeng.heap.MyHeap;
import com.qipeng.utils.binaryTreePrinter.BinaryTreeInfo;

import java.util.Comparator;

/**
 * 二叉堆
 */
@SuppressWarnings("unchecked")
public class MyBinaryHeap<E> extends MyAbstractHeap<E> implements MyHeap<E>, BinaryTreeInfo {

    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    public MyBinaryHeap(E[] elements, Comparator<E> comparator) {
        super(comparator);

        if (elements == null || elements.length == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            size = elements.length;
            int capacity = Math.max(elements.length, DEFAULT_CAPACITY);
            this.elements = (E[]) new Object[capacity];

            System.arraycopy(elements, 0, this.elements, 0, elements.length);

            // 批量建堆
            heapify();
        }
    }

    public MyBinaryHeap(E[] elements) {
        this(elements, null);
    }

    public MyBinaryHeap(Comparator<E> comparator) {
        this(null, comparator);
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    public MyBinaryHeap() {
        this(null, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }

        size = 0;
    }

    @Override
    public void add(E element) {
        elementNotNullCheck(element);
        ensureCapacity(size + 1);
        elements[size++] = element;
        siftUp(size - 1);
    }

    @Override
    public E get() {
        emptyCheck();

        return elements[0];
    }

    @Override
    public E remove() {
        emptyCheck();

        E root = elements[0];

        int lastIndex = --size;
        elements[0] = elements[lastIndex];
        elements[lastIndex] = null;

        siftDown(0);

        return root;
    }

    @Override
    public E replace(E element) {
        elementNotNullCheck(element);
        E root = null;

        if (size == 0) {
            elements[0] = element;
            size++;
        } else {
            root = elements[0];
            elements[0] = element;
            siftDown(0);
        }

        return root;
    }

    @Override
    public Object root() {
        return 0;
    }

    @Override
    public Object left(Object node) {
        int index = ((int) node << 1) + 1;

        return index >= size ? null : index;
    }

    @Override
    public Object right(Object node) {
        int index = ((int) node << 1) + 2;

        return index >= size ? null : index;
    }

    @Override
    public Object string(Object node) {
        return elements[(int) node];
    }

    /**
     * 保证容量
     *
     * @param capacity
     */
    private void ensureCapacity(int capacity) {
        int olcCapacity = elements.length;

        if (olcCapacity >= capacity) {
            return;
        }

        // 扩容 1.5 倍
        int newCapacity = olcCapacity + (olcCapacity >> 1);
        E[] newElements = (E[]) new java.lang.Object[newCapacity];

        if (size >= 0) System.arraycopy(elements, 0, newElements, 0, size);

        elements = newElements;
    }

    /**
     * 让index位置的元素上滤
     *
     * @param index
     */
    private void siftUp(int index) {
        E self = elements[index];

        while (index > 0) {
            int parentIndex = (index - 1) >> 1;
            E parent = elements[parentIndex];

            if (compare(self, parent) <= 0) break;

            // 将父元素存储在index位置
            elements[index] = parent;

            // 重新赋值index
            index = parentIndex;
        }

        elements[index] = self;
    }

    /**
     * 让index位置的元素下滤
     *
     * @param index
     */
    private void siftDown(int index) {
        E self = elements[index];
        int half = size >> 1;

        // 必须保证index位置是非叶子节点
        // index < 第一个叶子节点的索引（非叶子节点的数量）
        while (index < half) {
            // index的节点有2中情况
            // 1.只有左子节点
            // 2.同时有左右子节点

            // 默认为左子节点跟它进行比较
            int childIndex = (index << 1) + 1;
            E child = elements[childIndex];

            // 右子节点
            int rightIndex = childIndex + 1;

            // 选出左、右子节点最大的
            if (rightIndex < size && compare(elements[rightIndex], child) > 0) {
                childIndex = rightIndex;
                child = elements[rightIndex];
            }

            if (compare(self, child) >= 0) break;

            // 将子节点存放到index位置
            elements[index] = child;
            // 重新设置index
            index = childIndex;
        }

        elements[index] = self;
    }

    /**
     * 批量建堆
     */
    private void heapify() {
        // 自上而下的上滤
        // for (int i = 0; i < size; i++) {
        //     siftUp(i);
        // }

        // 自下而上的下滤
        // 只需考虑非叶子节点的情况
        for (int i = (size >> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void emptyCheck() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Heap is empty.");
        }
    }

    private void elementNotNullCheck(E ele) {
        if (ele == null) {
            throw new IllegalArgumentException("element must not be null.");
        }
    }
}
