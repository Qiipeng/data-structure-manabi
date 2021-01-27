package com.qipeng.linearList.linkedList;

import com.qipeng.linearList.MyAbstractList;


/**
 * 单向循环链表
 */
public class MySingleCircleLinkedList<E> extends MyAbstractList<E> {

    private Node<E> first;


    private static class Node<E> {
        E element;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(element).append("_").append(next.element);
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

        // 当在第一个节点处添加时，需要改变最后一个节点的 next
        if (index == 0) {
            first = new Node<>(element, first);
            size++;

            // 拿到最后一个节点
            // 需要注意，当只有一个节点时
            Node<E> last = size == 0 ? first : node(size - 1);
            last.next = first;
        } else {
            // 当前节点的对应上下节点
            // Node<E> prev = node(index - 1);
            // Node<E> next = node(index + 1);

            // 更新节点
            // Node<E> node = new Node<>(element, next);
            // prev.next = node;

            // 简洁写法
            Node<E> prev = node(index - 1);
            prev.next = new Node<>(element, prev.next);

            size++;
        }
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
        Node<E> node = first;

        if (index == 0) {
            if (size == 1) {
                first = null;
            } else {
                // 删除第一个节点
                // 循环链表中，最后一个节点的 next 指向被删除节点的 next
                Node<E> last = node(size - 1);
                first = first.next;
                last.next = first;
            }
        } else {
            Node<E> prev = node(index - 1);
            node = prev.next;

            prev.next = prev.next.next;
        }

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
    private Node<E> node(int index) {
        rangeCheck(index);

        Node<E> node = first;

        for (int i = 0; i < index; i++) {
            node = node.next;
        }

        return node;
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

        // 遍历链表使用 while 循环
        // while (node != null) {
        //     node = node.next;
        // }

        string.append("]");

        return string.toString();
    }
}
