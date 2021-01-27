package com.qipeng.set.impl;


import com.qipeng.set.MySet;
import com.qipeng.tree.redBlackTree.MyRBTree;

/**
 * 利用RBTree实现集合
 */
public class MyTreeSet<E> implements MySet<E> {

    private MyRBTree<E> tree = new MyRBTree<>();

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public boolean contains(E element) {
        return tree.contains(element);
    }

    @Override
    public void add(E element) {
        tree.add(element);
    }

    @Override
    public void remove(E element) {
        tree.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        tree.inorder(new com.qipeng.tree.binaryTree.MyBinaryTree.Visitor<>() {
            @Override
            public boolean visit(E element) {
                return visitor.visit(element);
            }
        });
    }
}
