package com.qipeng.linearList.queue;

import com.qipeng.linearList.MyList;
import com.qipeng.linearList.linkedList.MyLinkedList;

/**
 * 双端队列（链表实现）
 */
public class MyDeque<E> {

    private MyList<E> list;

    public MyDeque() {
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
     * 从尾入队
     *
     * @param element
     */
    public void enQueueRear(E element) {
        list.add(element);
    }

    /**
     * 从头入队
     *
     * @param element
     */
    public void enQueueFront(E element) {
        list.add(0, element);
    }

    /**
     * 从尾出队
     *
     * @return
     */
    public E deQueueRear() {
        return list.remove(list.size() - 1);
    }

    /**
     * 从头出队
     *
     * @return
     */
    public E deQueueFront() {
        return list.remove(0);
    }

    /**
     * 获取队尾
     *
     * @return
     */
    public E rear() {
        return list.get(list.size() - 1);
    }

    /**
     * 获取队头
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
