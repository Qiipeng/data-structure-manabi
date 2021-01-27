package com.qipeng.linearList.linkedList;

import com.qipeng.linearList.MyAbstractList;


/**
 * 双向循环链表
 */
public class MyCircleLinkedList<E> extends MyAbstractList<E> {

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
            last = new Node<>(oldLast, element, first);

            if (oldLast == null) { // 此时，这里为添加的第一个元素
                first = last;

                // 循环的上下节点指向自己
                first.next = last;
                last.prev = first;
            } else {
                oldLast.next = last;

                // 第一个节点的上一个节点，指向最后一个新添加的节点构成循环
                first.prev = last;
            }
        } else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> node = new Node<>(prev, element, next);
            next.prev = node;
            prev.next = node;

            if (next == first) { // index == 0
                first = node;
            }
        }

        size++;
    }

    @Override
    public int indexOf(Object element) {
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

        return remove(node(index));
    }

    public E remove(Node<E> node) {
        if (size == 1) {
            first = null;
            last = null;
        } else {
            Node<E> next = node.next;
            Node<E> prev = node.prev;
            prev.next = next;
            next.prev = prev;

            if (node == first) { // index == 0
                first = next;
            }

            if (node == last) { // index = size - 1
                last = prev;
            }
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
        StringBuilder string = new StringBuilder();
        string.append("size = ").append(size).append(", [");

        Node<E> node = first;

        for (int i = 0; i < size; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(node);

            node = node.next;
        }

        string.append("]");

        return string.toString();
    }


    // 发挥循环列表的最大威力

    // 约瑟夫问题求解
    private Node<E> current; // 用于指向节点

    /**
     * 让 current 指向头节点
     */
    public void reset() {
        current = first;
    }

    /**
     * 让 current 往后走一步
     *
     * @return
     */
    public E next() {
        if (current == null) {
            return null;
        }

        current = current.next;
        return current.element;
    }

    /**
     * 删除 current 指向的节点，删除成功后让 current 指向下一个节点
     *
     * @return
     */
    public E remove() {
        if (current == null) {
            return null;
        }

        Node<E> next = current.next;
        E element = remove(current);

        if (size == 0) {
            current = null;
        } else {
            current = next;
        }

        return element;
    }
}
