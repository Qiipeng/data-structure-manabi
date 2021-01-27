package com.qipeng.linearList;

public interface MyList<E> {

    int ELEMENT_NOT_FOUND = -1;


    /**
     * 元素数量
     *
     * @return
     */
    int size();

    /**
     * 是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 是否包含某个元素
     *
     * @param element
     * @return
     */
    boolean contains(E element);

    /**
     * 返回 index 位置对应的元素
     *
     * @param index
     * @return
     */
    E get(int index);

    /**
     * 设置 index 位置对应的元素
     *
     * @param index
     * @param element
     * @return 原来的元素
     */
    E set(int index, E element);

    /**
     * 在 index 位置添加元素
     *
     * @param index
     * @param element
     */
    void add(int index, E element);

    /**
     * 追加元素到尾部
     *
     * @param element
     */
    void add(E element);

    /**
     * 查看元素位置
     *
     * @param element
     * @return
     */
    int indexOf(E element);

    /**
     * 删除 index 位置对应的元素
     *
     * @param index
     * @return
     */
    E remove(int index);

    /**
     * 删除所有元素
     */
    void clear();
}
