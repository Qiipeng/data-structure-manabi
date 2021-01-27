package com.qipeng.trie;

import com.qipeng.trie.impl.MyTrieImpl;

public class TestMyTrie {

    public static void main(String[] args) {
        testMyTrie();
    }

    private static void testMyTrie() {
        MyTrie<Integer> trie = new MyTrieImpl<>();

        trie.add("cat", 1);
        trie.add("dog", 2);
        trie.add("catalog", 3);
        trie.add("cast", 4);
        trie.add("中文", 5);

        System.out.println(trie.size());
        System.out.println(trie.get("dog"));
        System.out.println(trie.get("中文"));
        System.out.println(trie.startWith("c"));
        System.out.println(trie.startWith("d"));
        System.out.println(trie.remove("dog"));
        System.out.println(trie.remove("cat"));
        System.out.println(trie.size());
    }
}
