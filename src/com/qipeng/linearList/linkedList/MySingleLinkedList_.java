package com.qipeng.linearList.linkedList;

import com.qipeng.linearList.MyAbstractList;

/**
 * 单向链表--虚拟头结点
 * 有时候为了让代码更加精简，统一所有节点的处理逻辑，可在前面增加一个虚拟头结点
 *
 * @param <E>
 */
public class MySingleLinkedList_<E> extends MyAbstractList<E> {

    private MySingleLinkedList_.Node<E> first;

    /**
     * 使用构造器添加虚拟头结点
     */
    public MySingleLinkedList_() {
        first = new Node<>(null, null);
    }

    private static class Node<E> {
        E element;
        MySingleLinkedList_.Node<E> next;

        Node(E element, MySingleLinkedList_.Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    @Override
    public E get(int index) {
        return (E) node(index).element;
    }

    @Override
    public E set(int index, E element) {
        MySingleLinkedList_.Node node = node(index);
        E oldEle = (E) node.element;
        node.element = element;

        return oldEle;
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        // 当 index 为 0 时，即可使用虚拟头结点
        // 反之，则按照规则去寻找上一个节点
        MySingleLinkedList_.Node<E> prev = index == 0 ? first : node(index - 1);
        prev.next = new MySingleLinkedList_.Node<>(element, prev.next);
        size++;
    }

    @Override
    public int indexOf(Object element) {
        if (element == null) {
            MySingleLinkedList_.Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (node.element == null) {
                    return i;
                }

                node = node.next;
            }
        } else {
            MySingleLinkedList_.Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (element.equals(node.element)) {
                    return i;
                }

                node = node.next;
            }
        }

        return ELEMENT_NOT_FOUND;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);

        MySingleLinkedList_.Node<E> prev = index == 0 ? first : node(index - 1);
        Node<E> node = prev.next;
        prev.next = node.next;
        size--;

        return node.element;
    }

    @Override
    public void clear() {
        first = null;
        size = 0;
    }

    /**
     * 根据索引返回对应的节点对象
     *
     * @param index
     * @return
     */
    private MySingleLinkedList_.Node<E> node(int index) {
        rangeCheck(index);

        MySingleLinkedList_.Node<E> node = first.next;

        for (int i = 0; i < index; i++) {
            node = node.next;
        }

        return node;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("size = ").append(size).append(", [");

        MySingleLinkedList_.Node<E> node = first.next;

        for (int i = 0; i < size; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(node.element);

            node = node.next;
        }

        // 遍历链表使用 while 循环
        // while (node != null) {
        //     node = node.next;
        // }

        string.append("]");

        return string.toString();
    }
}
