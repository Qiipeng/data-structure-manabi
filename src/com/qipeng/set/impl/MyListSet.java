package com.qipeng.set.impl;

import com.qipeng.linearList.MyList;
import com.qipeng.linearList.linkedList.MyLinkedList;
import com.qipeng.set.MySet;

/**
 * 利用linkedList实现集合
 */
public class MyListSet<E> implements MySet<E> {

    private MyList<E> list = new MyLinkedList<>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(E element) {
        return list.contains(element);
    }

    @Override
    public void add(E element) {
        // 注意不存放重复元素
        // if (com.qipeng.list.contains(element)) {
        //     return;
        // }
        // com.qipeng.list.add(element);

        // 等价于
        int index = list.indexOf(element);

        // 存在就覆盖
        if (index != list.ELEMENT_NOT_FOUND) {
            list.set(index, element);
        } else { // 不存在则添加
            list.add(element);
        }
    }

    @Override
    public void remove(E element) {
        int index = list.indexOf(element);

        if (index != list.ELEMENT_NOT_FOUND) {
            list.remove(index);
        }
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }

        int size = list.size();

        for (int i = 0; i < size; i++) {
            if (visitor.visit(list.get(i))) {
                return;
            }
        }
    }
}
