package com.qipeng.linearList.queue;

/**
 * 循环双端队列
 */
public class MyCirlcleDeque<E> {

    private int size;
    private int front; // 存储队头下标
    private E[] elementData;

    private static final int DEFAULT_CAPACITY = 10;

    public MyCirlcleDeque() {
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
     * 从尾入队
     *
     * @param element
     */
    public void enQueueRear(E element) {
        ensureCapacity(size + 1);

        elementData[index(size)] = element;
        size++;
    }

    /**
     * 从头入队
     *
     * @param element
     */
    public void enQueueFront(E element) {
        ensureCapacity(size + 1);

        front = index(-1);
        elementData[front] = element;
        size++;
    }

    /**
     * 从尾出队
     *
     * @return
     */
    public E deQueueRear() {
        int rearIndex = index(size - 1);
        E ele = elementData[rearIndex];

        elementData[rearIndex] = null;
        size--;

        return ele;
    }

    /**
     * 从头出队
     *
     * @return
     */
    public E deQueueFront() {
        E frontEle = elementData[front];
        elementData[front] = null;
        front = index(1);
        size--;

        return frontEle;
    }

    /**
     * 获取队尾
     *
     * @return
     */
    public E rear() {
        return elementData[index(size - 1)];
    }

    /**
     * 获取队头
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
        // 通过判断 index 是否是正，决定添加位置
        if (index < 0) {
            return index + elementData.length;
        }

        return index - (index >= elementData.length ? elementData.length : 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("capacity = ").append(elementData.length).append(" | ");

        for (int i = 0; i < elementData.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }

            sb.append(elementData[i]);
        }

        return sb.toString();
    }
}
