package com.qipeng.tree.redBlackTree;

import com.qipeng.tree.binaryTree.MyBBST;

import java.util.Comparator;

/**
 * 红黑树
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public class MyRBTree<E> extends MyBBST<E> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public MyRBTree() {
        this(null);
    }

    public MyRBTree(Comparator<E> comparator) {
        super(comparator);
    }

    private static class RBNode<E> extends Node<E> {

        boolean color = RED;

        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";

            if (color == RED) {
                str = "R_";
            }

            return str + element.toString();
        }
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBNode<>(element, parent);
    }

    @Override
    protected void afterAdd(Node<E> node) {
        Node<E> parent = node.parent;

        // 添加的是根节点，或者上溢到了根节点
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色，直接返回
        if (isBlack(parent)) {
            return;
        }

        Node<E> uncle = parent.sibling(); // 叔父节点
        Node<E> grand = red(parent.parent); // 祖父节点

        // 叔父节点是红色(B 树节点上溢）
        if (isRed(uncle)) {
            black(parent);
            black(uncle);

            // 把祖父节点当做是新添加的节点
            afterAdd(grand);
            return;
        }

        // 叔父节点不是红色
        if (parent.isLeftChild()) { // L
            if (node.isLeftChild()) { // LL
                black(parent);
            } else { // LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else { // R
            if (node.isLeftChild()) { // RL
                black(node);
                rotateRight(parent);
            } else { // RR
                black(parent);
            }
            rotateLeft(grand);
        }
    }

    // 去除replacement的情况
    @Override
    protected void afterRemove(Node<E> node) {
        // 如果删除的节点是红色
        // if (isRed(node)) { return; }

        // 如果删除的节点是红色
        // 或者用以取代 node 的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }

        Node<E> parent = node.parent;
        // 删除的是根节点
        if (parent == null) {
            return;
        }

        // 删除的是黑色叶子节点(下溢)
        // 判断被删除的 node 是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<E> sibling = left ? parent.right : parent.left;

        if (left) { // 被删除的节点在左边，兄弟节点在右边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 兄弟节点必然是黑色
            // 兄弟节点没有红色子节点
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 父节点要向下和兄弟节点合并
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);

                if (isParentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有一个红色子节点
                // 向兄弟节点借元素
                // 兄弟节点的左边是黑森，兄弟节点要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }

                // 先染色，再旋转
                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        } else { // 被删除的节点在右边 ，兄弟节点在左边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 兄弟节点必然是黑色
            // 兄弟节点没有红色子节点
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 父节点要向下和兄弟节点合并
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);

                if (isParentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有一个红色子节点
                // 向兄弟节点借元素
                // 兄弟节点的左边是黑森，兄弟节点要先旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left;
                }

                // 先染色，再旋转
                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }
    }

    // replacement参数情况
    /*
    @Override
    protected void afterRemove(Node<E> node, Node<E> replacement) {
        // 如果删除的节点是红色
        if (isRed(node)) {
            return;
        }

        //  用以取代 node 的子节点是红色
        if (isRed(replacement)) {
            black(replacement);
            return;
        }

        Node<E> parent = node.parent;
        // 删除的是根节点
        if (parent == null) {
            return;
        }

        // 删除的是黑色叶子节点(下溢)
        // 判断被删除的 node 是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<E> sibling = left ? parent.right : parent.left;

        if (left) { // 被删除的节点在左边，兄弟节点在右边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 兄弟节点必然是黑色
            // 兄弟节点没有红色子节点
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 父节点要向下和兄弟节点合并
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);

                if (isParentBlack) {
                    afterRemove(parent, null);
                }
            } else { // 兄弟节点至少有一个红色子节点
                // 向兄弟节点借元素
                // 兄弟节点的左边是黑森，兄弟节点要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }

                // 先染色，再旋转
                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        } else { // 被删除的节点在右边 ，兄弟节点在左边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 兄弟节点必然是黑色
            // 兄弟节点没有红色子节点
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 父节点要向下和兄弟节点合并
                boolean isParentBlack = isBlack(parent);
                black(parent);
                red(sibling);

                if (isParentBlack) {
                    afterRemove(parent, null);
                }
            } else { // 兄弟节点至少有一个红色子节点
                // 向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟节点要先旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left;
                }

                // 先染色，再旋转
                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }
    }
    */

    /**
     * 染色操作
     *
     * @param node
     * @param color
     * @return
     */
    private Node<E> color(Node<E> node, boolean color) {
        if (node == null) {
            return null;
        }

        ((RBNode<E>) node).color = color;

        return node;
    }

    /**
     * 染成红色
     *
     * @param node
     * @return
     */
    private Node<E> red(Node<E> node) {
        return color(node, RED);
    }

    /**
     * 染成黑色
     *
     * @param node
     * @return
     */
    private Node<E> black(Node<E> node) {
        return color(node, BLACK);
    }

    /**
     * 节点颜色
     *
     * @param node
     * @return
     */
    private boolean colorOf(Node<E> node) {
        return node == null ? BLACK : ((RBNode<E>) node).color;
    }

    /**
     * 节点是否为黑色
     *
     * @param node
     * @return
     */
    private boolean isBlack(Node<E> node) {
        return colorOf(node) == BLACK;
    }

    /**
     * 节点是否为红色
     *
     * @param node
     * @return
     */
    private boolean isRed(Node<E> node) {
        return colorOf(node) == RED;
    }
}
