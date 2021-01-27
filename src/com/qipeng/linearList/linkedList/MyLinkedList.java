package com.qipeng.linearList.linkedList;

import com.qipeng.linearList.MyAbstractList;

/**
 * 双向链表
 */
public class MyLinkedList<E> extends MyAbstractList<E> {

    private Node<E> first;
    private Node<E> last;

    private static class Node<E> {
        E element;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E element, Node<E> next) {
            this.prev = prev;
            this.element = element;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (prev != null) {
                sb.append(prev.element);
            } else {
                sb.append("null");
            }

            sb.append("_").append(element).append("_");

            if (next != null) {
                sb.append(next.element);
            } else {
                sb.append("null");
            }

            return sb.toString();
        }
    }

    @Override
    public E get(int index) {
        return (E) node(index).element;
    }

    @Override
    public E set(int index, E element) {
        Node node = node(index);
        E oldEle = (E) node.element;
        node.element = element;

        return oldEle;
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        if (index == size) { // 在尾添加元素
            Node oldLast = last;
            last = new Node<>(oldLast, element, null);

            if (oldLast == null) { // 此时，这里为添加的第一个元素
                first = last;
            } else {
                oldLast.next = last;
            }
        } else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> node = new Node<>(prev, element, next);
            next.prev = node;

            if (prev == null) { // index == 0
                first = node;
            } else {
                prev.next = node;
            }
        }

        size++;
    }

    @Override
    public int indexOf(E element) {
        if (element == null) {
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (node.element == null) {
                    return i;
                }

                node = node.next;
            }
        } else {
            Node<E> node = first;
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

        Node<E> node = node(index);
        Node<E> next = node.next;
        Node<E> prev = node.prev;

        if (prev == null) { // index == 0
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) { // index = size - 1
            last = prev;
        } else {
            next.prev = prev;
        }

        size--;

        return node.element;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    /**
     * 根据索引返回对应的节点对象
     *
     * @param index
     * @return
     */
    private Node<E> node(int index) {
        rangeCheck(index);

        // 双向链表，判断遍历开始节点
        // 从头开始遍历
        if (index < size >> 1) {
            Node<E> node = first;

            for (int i = 0; i < index; i++) {
                node = node.next;
            }

            return node;
        } else { // 从尾开始遍历
            Node<E> node = last;

            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }

            return node;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size = ").append(size).append(", [");

        Node<E> node = first;

        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(", ");
            }

            sb.append(node);

            node = node.next;
        }

        sb.append("]");

        return sb.toString();
    }
}
