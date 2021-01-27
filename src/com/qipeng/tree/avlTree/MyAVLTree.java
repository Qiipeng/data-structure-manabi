package com.qipeng.tree.avlTree;

import com.qipeng.tree.binaryTree.MyBBST;

import java.util.Comparator;

/**
 * AVL树--平衡树的一种
 */
@SuppressWarnings({"JavaDoc", "IfStatementWithIdenticalBranches"})
public class MyAVLTree<E> extends MyBBST<E> {

    public MyAVLTree() {
        this(null);
    }

    public MyAVLTree(Comparator<E> comparator) {
        super(comparator);
    }

    private static class AVLNode<E> extends Node<E> {
        int height = 1; // 高度(平衡因子）

        AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }

        int balanceFactor() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;

            return leftHeight - rightHeight;
        }

        void updateHeight() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;

            height = 1 + Math.max(leftHeight, rightHeight);
        }

        Node<E> tallerChild() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;

            if (leftHeight > rightHeight) {
                return left;
            }

            if (leftHeight < rightHeight) {
                return right;
            }

            return isLeftChild() ? left : right;
        }

        @Override
        public String toString() {
            String parentString = "null";

            if (parent != null) {
                parentString = parent.element.toString();
            }

            return element + "_p(" + parentString + ")_h(" + height + ")";
        }
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element, parent);
    }

    @Override
    protected void afterAdd(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                // 更新高度
                updateHeight(node);
            } else {
                // 恢复平衡
                reBalance(node);
                // 整棵树恢复平衡
                break;
            }
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                // 更新高度
                updateHeight(node);
            } else {
                // 恢复平衡
                reBalance(node);
            }
        }
    }

    @Override
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);

        // 更新高度
        updateHeight(grand);
        updateHeight(parent);
    }

    @Override
    protected void rotate(Node<E> r, Node<E> a, Node<E> b, Node<E> c, Node<E> d, Node<E> e, Node<E> f, Node<E> g) {
        super.rotate(r, a, b, c, d, e, f, g);

        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
    }

    /**
     * 是否平衡
     *
     * @param node
     * @return
     */
    private boolean isBalanced(Node<E> node) {
        return Math.abs(((AVLNode<E>) node).balanceFactor()) <= 1;
    }

    /**
     * 更新高度
     *
     * @param node
     */
    private void updateHeight(Node<E> node) {
        ((AVLNode<E>) node).updateHeight();
    }

    /**
     * 恢复平衡
     *
     * @param grand 高度最低的那个不平衡节点
     */
    private void reBalance(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>) grand).tallerChild();
        Node<E> node = ((AVLNode<E>) parent).tallerChild();

        if (parent.isLeftChild()) { // L
            if (node.isLeftChild()) { // LL
                rotate(grand, node.left, node, node.right, parent, parent.right, grand, grand.right);
            } else { // LR
                rotate(grand, parent.left, parent, node.left, node, node.right, grand, grand.right);
            }
        } else { // R
            if (node.isLeftChild()) { // RL
                rotate(grand, grand.left, grand, node.left, node, node.right, parent, parent.right);
            } else { // RR
                rotate(grand, grand.left, grand, parent.left, parent, node.left, node, node.right);
            }
        }
    }

    /**
     * 恢复平衡
     *
     * @param grand 高度最低的那个不平衡节点
     */
    private void reBalance2(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>) grand).tallerChild();
        Node<E> node = ((AVLNode<E>) parent).tallerChild();

        if (parent.isLeftChild()) { // L
            if (node.isLeftChild()) { // LL
                rotateRight(grand);
            } else { // LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        } else { // R
            if (node.isLeftChild()) { // RL
                rotateRight(parent);
                rotateLeft(grand);
            } else { // RR
                rotateLeft(grand);
            }
        }
    }

}
