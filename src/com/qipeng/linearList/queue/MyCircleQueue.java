package com.qipeng.linearList.queue;


/**
 * 循环队列（数组实现）
 */
public class MyCircleQueue<E> {

    private int size;
    private int front; // 存储队头下标
    private E[] elementData;

    private static final int DEFAULT_CAPACITY = 10;

    public MyCircleQueue() {
        elementData = (E[]) new Object[DEFAULT_CAPACITY];
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
        return size == 0;
    }

    /**
     * 入队
     *
     * @param element
     */
    public void enQueue(E element) {
        ensureCapacity(size + 1);

        elementData[index(size)] = element;
        size++;
    }

    /**
     * 出队
     *
     * @return
     */
    public E deQueue() {
        E frontEle = elementData[front];
        elementData[front] = null;
        front = index(1);
        size--;

        return frontEle;
    }

    /**
     * 获取队列的头元素
     *
     * @return
     */
    public E front() {
        return elementData[front];
    }

    /**
     * 清空队列
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[index(i)] = null;
        }

        front = 0;
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

        for (int i = 0; i < size; i++) {
            newElementData[i] = elementData[index(i)];
        }

        elementData = newElementData;
        // 重置 front
        front = 0;
    }

    /**
     * 维护索引保持循环
     *
     * @param index
     * @return
     */
    private int index(int index) {
        index += front;

        // 这里的取模运算可以进行优化
        // return ((front + index) % elementData.length);
        return index - (index >= elementData.length ? elementData.length : 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < elementData.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }

            sb.append(elementData[i]);
        }

        return sb.toString();
    }
}
