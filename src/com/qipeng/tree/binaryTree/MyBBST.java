package com.qipeng.tree.binaryTree;

import java.util.Comparator;

/**
 * 平衡二叉树
 */
@SuppressWarnings({"JavaDoc", "SpellCheckingInspection"})
public class MyBBST<E> extends MyBST<E> {

    public MyBBST() {
        this(null);
    }

    public MyBBST(Comparator<E> comparator) {
        super(comparator);
    }

    /**
     * 左旋转
     *
     * @param grand
     */
    protected void rotateLeft(Node<E> grand) {
        Node<E> parent = grand.right;
        Node<E> child = parent.left;
        // 修改指向
        grand.right = child;
        parent.left = grand;

        // 更新对应的父节点(grand parent child)
        // 更新高度
        afterRotate(grand, parent, child);
    }

    /**
     * 右旋转
     *
     * @param grand
     */
    protected void rotateRight(Node<E> grand) {
        Node<E> parent = grand.left;
        Node<E> child = parent.right;
        grand.left = child;
        parent.right = grand;

        afterRotate(grand, parent, child);
    }

    /**
     * 更新父节点以及高度
     *
     * @param grand
     * @param parent
     * @param child
     */
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        // 更新 parent 成为子树的根节点
        parent.parent = grand.parent;

        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else { // grand 是 root 节点
            root = parent;
        }

        // 更新 child 的 parent
        if (child != null) {
            child.parent = grand;
        }

        // 更新 grand 的 parent
        grand.parent = parent;
    }

    /**
     * 统一所有旋转操作
     *
     * @param r 子树的根节点
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @param f
     * @param g
     */
    protected void rotate(
            Node<E> r,
            Node<E> a, Node<E> b, Node<E> c,
            Node<E> d,
            Node<E> e, Node<E> f, Node<E> g) {
        // 让 d 成为这棵子树的根节点
        d.parent = r.parent;

        if (r.isLeftChild()) {
            r.parent.left = d;
        } else if (r.isRightChild()) {
            r.parent.right = d;
        } else {
            root = d;
        }

        // a-b-c
        b.left = a;

        if (a != null) {
            a.parent = b;
        }

        b.right = c;

        if (c != null) {
            c.parent = b;
        }

        // e-f-g
        f.left = e;

        if (e != null) {
            e.parent = f;
        }

        f.right = g;

        if (g != null) {
            g.parent = f;
        }

        // b-d-f
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;
    }
}
