package com.qipeng.linearList.stack;

import com.qipeng.linearList.MyList;
import com.qipeng.linearList.arrayList.MyArrayList;

/**
 * 栈（链表实现）
 */
public class MyStack<E> {

    // 使用组合
    private MyList<E> list;

    public MyStack() {
        list = new MyArrayList<>();
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
     * 入栈
     *
     * @param element
     */
    public void push(E element) {
        list.add(element);

    }

    /**
     * 出栈
     *
     * @return
     */
    public E pop() {
        return list.remove(list.size() - 1);
    }

    /**
     * 获取栈顶元素
     *
     * @return
     */
    public E peek() {
        return list.get(list.size() - 1);
    }

    public void clear() {
        list.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }

            sb.append(list.get(i));
        }

        sb.append("]");

        return sb.toString();
    }
}
