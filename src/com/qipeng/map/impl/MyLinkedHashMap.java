package com.qipeng.map.impl;

import java.util.Objects;

/**
 * LinkedHashMap
 * 在HashMap的基础上维护元素的添加顺序，使得遍历的结果是遵从添加顺序的
 */
public class MyLinkedHashMap<K, V> extends MyHashMap<K, V> {

    private LinkedNode<K, V> first;
    private LinkedNode<K, V> last;

    private static class LinkedNode<K, V> extends Node<K, V> {
        LinkedNode<K, V> prev;
        LinkedNode<K, V> next;

        public LinkedNode(K key, V value, Node<K, V> parent) {
            super(key, value, parent);
        }
    }

    @Override
    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        LinkedNode<K, V> node = new LinkedNode<>(key, value, parent);

        if (first == null) {
            first = last = node;
        } else {
            last.next = node;
            node.prev = last;
            last = node;
        }

        return node;
    }

    @Override
    public void clear() {
        super.clear();

        first = null;
        last = null;
    }

    @Override
    public boolean containsValue(V value) {
        LinkedNode<K, V> node = first;

        while (node != null) {
            if (Objects.equals(value, node.value)) {
                return true;
            }

            node = node.next;
        }

        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) {
            return;
        }

        LinkedNode<K, V> node = first;

        while (node != null) {
            if (visitor.visit(node.key, node.value)) {
                return;
            }

            node = node.next;
        }
    }

    /**
     * 修复删除后的链表指向
     * <p>
     * 删除度为2的节点node时
     * 需要注意更换node与前驱/后继节点的连接位置
     *
     * @param replacedNode 原本要删除的节点
     * @param removedNode  真正被删除的节点
     */
    @Override
    protected void afterRemove(Node<K, V> replacedNode, Node<K, V> removedNode) {
        LinkedNode<K, V> node1 = (LinkedNode<K, V>) replacedNode;
        LinkedNode<K, V> node2 = (LinkedNode<K, V>) removedNode;

        // 删除度为2的节点
        if (node1 != null) {
            // 交换两节点在链表中的位置
            // 交换prev
            LinkedNode<K, V> tmp = node1.prev;
            node1.prev = node2.prev;
            node2.prev = tmp;

            if (node1.prev == null) {
                first = node1;
            } else {
                node1.prev.next = node1;
            }

            if (node2.prev == null) {
                first = node2;
            } else {
                node2.prev.next = node2;
            }

            // 交换next
            tmp = node1.next;
            node1.next = node2.next;
            node2.next = tmp;

            if (node1.next == null) {
                last = node1;
            } else {
                node1.next.prev = node1;
            }

            if (node2.next == null) {
                last = node2;
            } else {
                node2.next.prev = node2;
            }
        }

        LinkedNode<K, V> prev = node2.prev;
        LinkedNode<K, V> next = node2.next;

        // 删除的是头节点
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }

        // 删除的是尾节点
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }
    }
}