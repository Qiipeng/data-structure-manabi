package com.qipeng.linearList.arrayList;

import com.qipeng.linearList.MyAbstractList;

/**
 * 有缩容操作的动态数组
 */
@SuppressWarnings({"unchecked", "ManualMinMaxCalculation", "DuplicatedCode", "JavaDoc", "ManualArrayCopy"})
public class MyArrayList_<E> extends MyAbstractList<E> {

    private E[] elementData; // 所有元素

    private static final int DEFAULT_CAPACITY = 10;

    public MyArrayList_() {
        elementData = (E[]) new Object[DEFAULT_CAPACITY];
    }

    public MyArrayList_(int capacity) {
        capacity = (capacity < DEFAULT_CAPACITY) ? DEFAULT_CAPACITY : capacity;
        elementData = (E[]) new Object[capacity];
    }

    /**
     * 返回 index 位置对应的元素
     *
     * @param index
     * @return
     */
    @Override
    public E get(int index) { // O(1)
        rangeCheck(index);

        return elementData[index];
    }

    /**
     * 设置 index 位置对应的元素
     *
     * @param index
     * @param element
     * @return 原来的元素
     */
    @Override
    public E set(int index, E element) { // O(1)
        rangeCheck(index);

        E oldEle = elementData[index];
        elementData[index] = element;

        return oldEle;
    }

    /**
     * 在 index 位置添加元素
     *
     * @param index
     * @param element
     */
    @Override
    public void add(int index, E element) {
        // 不允许存储 null
        // if (element == null) {
        //     return;
        // }

        rangeCheckForAdd(index);
        ensureCapacity(size + 1);

        for (int i = size; i > index; i--) {
            elementData[i] = elementData[i - 1];
        }

        elementData[index] = element;
        size++;
    }

    /**
     * 删除 index 位置对应的元素
     *
     * @param index
     * @return
     */
    @Override
    public E remove(int index) {
        rangeCheck(index);

        E oldEle = elementData[index];

        System.arraycopy(elementData, index + 1, elementData, index, size - 1 - index);

        // for (int i = index; i < size; i++) {
        //     elementData[i] = elementData[i + 1];
        // }

        // size--;
        // elementData[size] = null;
        elementData[--size] = null;

        trim();

        return oldEle;
    }

    /**
     * 删除指定元素
     *
     * @param element
     */
    public E remove(E element) {
        remove(indexOf(element));
        return null;
    }

    /**
     * 查看元素位置
     *
     * @param element
     * @return
     */
    @Override
    public int indexOf(E element) {
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(elementData[i])) {
                    return i;
                }
            }
        }

        return ELEMENT_NOT_FOUND;
    }

    /**
     * 删除所有元素
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }

        size = 0;
    }


    /**
     * 保证容量
     *
     * @param capacity
     */
    private void ensureCapacity(int capacity) {
        int olcCapacity = elementData.length;

        if (olcCapacity >= capacity) {
            return;
        }

        // 扩容 1.5 倍
        int newCapacity = olcCapacity + (olcCapacity >> 1);
        E[] newElementData = (E[]) new Object[newCapacity];

        for (int i = 0; i < newCapacity; i++) {
            newElementData[i] = elementData[i];
        }

        elementData = newElementData;
    }

    /**
     * 缩容数组--裁剪
     */
    private void trim() {
        int capacity = elementData.length;
        int newCapacity = capacity >> 1;

        // 不需要缩容的情况
        // 当前元素数量大于容量的一半
        // 当前容量小于等于默认容量
        if (size >= newCapacity || capacity <= DEFAULT_CAPACITY) {
            return;
        }

        // 剩余容量过多，需要缩容处理
        E[] newElementData = (E[]) new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newElementData[i] = elementData[i];
        }

        elementData = newElementData;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("size = ").append(size).append(", [");

        for (int i = 0; i < size; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(elementData[i]);

            // if (i != size - 1) {
            //     string.append(", ");
            // }
        }

        string.append("]");

        return string.toString();
    }
}
