package com.qipeng.set.impl;

import com.qipeng.map.MyMap;
import com.qipeng.map.impl.MyHashMap;
import com.qipeng.map.impl.MyLinkedHashMap;
import com.qipeng.set.MySet;

/**
 * 利用linkedHashMap实现集合
 */
public class MyLinkedHashSet<E> implements MySet<E> {

    MyHashMap<E, Object> map = new MyLinkedHashMap<>();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(E element) {
        return map.containsKey(element);
    }

    @Override
    public void add(E element) {
        map.put(element, null);
    }

    @Override
    public void remove(E element) {
        map.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        map.traversal(new MyMap.Visitor<E, Object>() {
            @Override
            public boolean visit(E key, Object value) {
                return visitor.visit(key);
            }
        });
    }
}
