package com.qipeng.trie;

/**
 * Trie也叫字典树、前缀树（Prefix Tree）、单词查找树
 * Trie搜索字符串的效率主要跟字符串的长度有关
 */
public interface MyTrie<E> {

    int size();

    boolean isEmpty();

    void clear();

    boolean contains(String key);

    E get(String key);

    E add(String key, E value);

    E remove(String key);

    boolean startWith(String prefix);
}
