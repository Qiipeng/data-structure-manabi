package com.qipeng.trie.impl;

import com.qipeng.trie.MyTrie;

import java.util.HashMap;


@SuppressWarnings("ConstantConditions")
public class MyTrieImpl<E> implements MyTrie<E> {

    private int size;
    private Node<E> root;

    private static class Node<E> {
        E value;
        boolean word; // 是否为单词额结尾（是否为一个完整的单词）
        HashMap<Character, Node<E>> children; // 存储子节点（可以对应多个子节点）
        Node<E> parent;
        Character character;

        public Node(Node<E> parent) {
            this.parent = parent;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean contains(String key) {
        Node<E> node = node(key);
        return node != null && node.word;
    }

    @Override
    public E get(String key) {
        Node<E> node = node(key);

        return node != null && node.word ? node.value : null;
    }

    @Override
    public E add(String key, E value) {
        keyCheck(key);

        // 创建根节点
        if (root == null) {
            root = new Node<>(null);
        }

        Node<E> node = root;
        int len = key.length();

        for (int i = 0; i < len; i++) {
            char c = key.charAt(i);
            boolean emptyChildren = node.children == null;
            Node<E> chileNode = emptyChildren ? null : node.children.get(c);

            if (chileNode == null) {
                chileNode = new Node<>(node);
                chileNode.character = c;
                node.children = emptyChildren ? new HashMap<>() : node.children;
                node.children.put(c, chileNode);
            }

            node = chileNode;
        }

        // 覆盖
        if (node.word) {
            E oleValue = node.value;
            node.value = value;

            return oleValue;
        }

        // 新增一个单词
        node.word = true;
        node.value = value;
        size++;

        return null;
    }

    @Override
    public E remove(String key) {
        // 找到最后一个节点
        Node<E> node = node(key);
        // 如果不是单词结尾，不用作任何处理
        if (node == null || !node.word) return null;

        size--;
        E oldValue = node.value;

        // 如果还有子节点
        if (node.children != null && !node.children.isEmpty()) {
            node.word = false;
            node.value = null;

            return oldValue;
        }

        // 如果没有子节点
        Node<E> parent;

        while ((parent = node.parent) != null) {
            parent.children.remove(node.character);

            if (parent.word || parent.children.isEmpty()) break;

            node = parent;
        }

        return oldValue;
    }

    @Override
    public boolean startWith(String prefix) {
        keyCheck(prefix);

        Node<E> node = root;
        int len = prefix.length();

        for (int i = 0; i < len; i++) {
            if (node == null || node.children == null || node.children.isEmpty()) return false;

            char c = prefix.charAt(i);
            node = node.children.get(c);
        }

        return true;
    }

    private Node<E> node(String key) {
        if (root == null) return null;

        keyCheck(key);
        Node<E> node = root;
        int len = key.length();

        for (int i = 0; i < len; i++) {
            if (node == null || node.children == null || node.children.isEmpty()) return null;

            char c = key.charAt(i);
            node = node.children.get(c);
        }

        return node.word ? node : null;
    }

    private void keyCheck(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key must not be empty");
        }
    }
}
