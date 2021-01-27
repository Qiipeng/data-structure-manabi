package com.qipeng.set;

/**
 * 集合接口
 */
public interface MySet<E> {

    int size();

    boolean isEmpty();

    void clear();

    boolean contains(E element);

    void add(E element);

    void remove(E element);

    /**
     * 遍历集合
     *
     * @param visitor
     */
    void traversal(Visitor<E> visitor);

    public static abstract class Visitor<E> {
        boolean stop;

        public abstract boolean visit(E element);
    }
}
