package com.qipeng.linearList.queue;

import com.qipeng.linearList.MyList;
import com.qipeng.linearList.linkedList.MyLinkedList;


/**
 * 队列（链表实现）
 */
public class MyQueue<E> {

    private MyList<E> list;

    public MyQueue() {
        list = new MyLinkedList<>();

    }

    /**
     * 元素数量
     *
     * @return
     */
    public int size() {
        return list.size();
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * 入队
     *
     * @param element
     */
    public void enQueue(E element) {
        list.add(element);
    }

    /**
     * 出队
     *
     * @return
     */
    public E deQueue() {
        return list.remove(0);
    }

    /**
     * 获取队列的头元素
     *
     * @return
     */
    public E front() {
        return list.get(0);
    }

    /**
     * 清空队列
     */
    public void clear() {
        list.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = list.size() - 1; i >= 0; i--) {
            sb.append(list.get(i));

            if (i != 0) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}
