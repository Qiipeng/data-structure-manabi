package com.qipeng.heap.impl;

import com.qipeng.heap.MyHeap;

import java.util.Comparator;

/**
 * 抽象堆父类（抽象类可以只实现接口中的几个方法）
 */
@SuppressWarnings("unchecked")
public abstract class MyAbstractHeap<E> implements MyHeap<E> {

    protected int size;
    protected Comparator<E> comparator;

    public MyAbstractHeap(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public MyAbstractHeap() {
        this(null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    protected int compare(E e1, E e2) {
        return comparator != null ? comparator.compare(e1, e2) : ((Comparable<E>) e1).compareTo(e2);
    }
}
