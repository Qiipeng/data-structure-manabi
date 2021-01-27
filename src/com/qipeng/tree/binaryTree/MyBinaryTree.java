package com.qipeng.tree.binaryTree;

import com.qipeng.utils.binaryTreePrinter.BinaryTreeInfo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 二叉树父类
 */
@SuppressWarnings({"Duplicates", "unchecked", "JavaDoc", "rawtypes", "unused"})
public class MyBinaryTree<E> implements BinaryTreeInfo {

    protected int size; // 节点数
    protected Node<E> root; // 根节点

    /**
     * 成员变量用来存储元素
     *
     * @param <E>
     */
    protected static class Node<E> {
        public E element;
        public Node<E> left;
        public Node<E> right;
        public Node<E> parent;

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        /**
         * 是否是叶子节点
         *
         * @return
         */
        public boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * 是否有两个子节点
         *
         * @return
         */
        public boolean withTwoLeaf() {
            return left != null && right != null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        /**
         * 叔父节点
         *
         * @return
         */
        public Node<E> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }

            return null;
        }

        @Override
        public String toString() {
            return " " + element + " ";
        }
    }

    /**
     * 让子类可以创建符合自己要求的节点
     *
     * @param element
     * @param parent
     * @return
     */
    protected Node<E> createNode(E element, Node<E> parent) {
        return new Node<>(element, parent);
    }

    /**
     * 元素数量
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 清空树
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * 二叉树高度--迭代
     *
     * @return
     */
    public int height() {
        if (root == null) {
            return 0;
        }

        int height = 0; // 树的高度
        int levelSize = 1; // 存储每一层的元素数量

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node<E> node = queue.poll(); // 将队头节点出队，进行访问
            levelSize--;

            if (node.left != null) { // 左子节点入队
                queue.offer(node.left);
            }

            if (node.right != null) { // 右子节点入队
                queue.offer(node.right);
            }

            if (levelSize == 0) { // 即将访问下一层
                levelSize = queue.size();
                height++;
            }
        }

        return height;
    }

    /**
     * 二叉树高度--递归
     *
     * @return
     */
    public int height2() {
        return height(root);
    }

    private int height(Node<E> node) {
        if (node == null) {
            return 0;
        }

        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * 是否是完全二叉树
     * 步骤：
     * 如果树为空，返回 false
     * 如果树不为空，开始层序遍历二叉树（用队列）
     * 如果 node.left != null && node.right != null，将 node.left、node.right 按顺序入队
     * 如果 node.left == null && node.right != null，返回 false
     * 如果 node.left != null && node.right == null 或者 node.left == null && node.left == null
     * 那么会面遍历的节点应该都为叶子节点，才是完全二叉树；否则返回 false
     * 遍历结束，返回 true
     *
     * @return
     */
    public boolean isComplete() {
        if (root == null) {
            return false;
        }

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean left = false;

        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();

            if (left && !node.isLeaf()) {
                return false;
            }

            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) {
                // node.left == null && node.right != null
                return false;
            }

            if (node.right != null) {
                queue.offer(node.right);
            } else {
                // node.left == null && node.right == null
                // node.left != null && node.right == null
                left = true;
            }
        }

        return true;
    }

    /**
     * 该接口让外界自己设计遍历树的方法
     *
     * @param <E>
     */
    @SuppressWarnings("WeakerAccess")
    public static abstract class Visitor<E> {

        boolean stop;

        /**
         * @param element
         * @return true->停止遍历；false->继续遍历
         */
        public abstract boolean visit(E element);
    }

    /**
     * 层序遍历,允许外界自定义遍历逻辑
     * 从上到下，从左到右依次访问每一个节点
     *
     * @param visitor
     */
    public void levelOrder(Visitor<E> visitor) {
        if (root == null || visitor == null) {
            return;
        }

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node<E> node = queue.poll(); // 将队头节点出队，进行访问

            if (visitor.visit(node.element)) {
                return;
            }

            if (node.left != null) { // 左子节点入队
                queue.offer(node.left);
            }

            if (node.right != null) { // 右子节点入队
                queue.offer(node.right);
            }
        }
    }

    /**
     * 前序遍历
     * 根节点->前序遍历左子树->前序遍历右子树
     *
     * @param visitor
     */
    public void preorder(Visitor<E> visitor) {
        if (visitor != null) {
            preorder(root, visitor);
        }
    }

    private void preorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) {
            return;
        }

        visitor.stop = visitor.visit(node.element);
        preorder(node.left, visitor);
        preorder(node.right, visitor);
    }

    /**
     * 前序遍历非递归实现
     *
     * @param visitor
     */
    public void preorder1(Visitor<E> visitor) {
        if (visitor == null || root == null) return;

        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>();

        while (true) {
            if (node != null) {
                // 访问node节点
                if (visitor.visit(node.element)) return;

                // 将右子节点入栈
                if (node.right != null) {
                    stack.push(node.right);
                }

                // 向左走
                node = node.left;
            } else if (stack.isEmpty()) {
                return;
            } else {
                node = stack.pop();
            }
        }
    }

    /**
     * 中序遍历
     * 中序遍历左子树->根节点->中序遍历右节点
     * 二叉搜索树的中序遍历结果是升序或降序的
     *
     * @param visitor
     */
    public void inorder(Visitor<E> visitor) {
        if (visitor != null) {
            inorder(root, visitor);
        }
    }

    private void inorder(Node<E> node, Visitor visitor) {
        if (node == null || visitor.stop) {
            return;
        }

        inorder(node.left, visitor);

        if (visitor.stop) {
            return;
        }

        visitor.stop = visitor.visit(node.element);
        inorder(node.right, visitor);
    }

    /**
     * 中序遍历非递归实现
     *
     * @param visitor
     */
    public void inorder1(Visitor<E> visitor) {
        if (visitor == null || root == null) return;

        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>();

        while (true) {
            if (node != null) {
                stack.push(node);
                node = node.left;
            } else if (stack.isEmpty()) {
                return;
            } else {
                node = stack.pop();

                // 访问node节点
                if (visitor.visit(node.element)) return;

                // 让右节点进行中序遍历
                node = node.right;
            }
        }
    }

    /**
     * 后序遍历
     * 后序遍历左子树->后序遍历右子树->根节点
     *
     * @param visitor
     */
    public void postorder(Visitor<E> visitor) {
        if (visitor != null) {
            postorder(root, visitor);
        }
    }

    private void postorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) { // 此处的 visitor.stop 为了停止递归
            return;
        }

        postorder(node.left, visitor);
        postorder(node.right, visitor); // visitor.stop == true 时

        if (visitor.stop) { // 之后也不应该继续调用
            return;
        }

        visitor.stop = visitor.visit(node.element);
    }

    /**
     * 后序遍历非递归实现
     *
     * @param visitor
     */
    public void postorder1(Visitor<E> visitor) {
        if (visitor == null || root == null) return;

        // 记录上一次弹出访问的节点
        Node<E> prev = null;
        Stack<Node<E>> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node<E> top = stack.peek();

            if (top.isLeaf() || (prev != null && prev.parent == top)) {
                prev = stack.pop();

                // 访问node节点
                if (visitor.visit(prev.element)) return;
            } else {
                if (top.right != null) {
                    stack.push(top.right);
                }

                if (top.left != null) {
                    stack.push(top.left);
                }
            }
        }
    }

    /**
     * 前驱节点
     * 中序遍历时的前一个节点
     * 如果是二叉搜索树，前驱节点就是前一个比它小的节点
     *
     * @param node
     * @return
     */
    protected Node<E> predecessor(Node<E> node) {
        if (node == null) {
            return null;
        }

        Node<E> p = node.left;

        // 前驱节点在左子树当中（node.left.right.right...）
        if (p != null) {
            while (p.right != null) {
                p = p.right;
            }

            return p;
        }

        // 从父节点、祖父节点中寻找（node.parent.parent.parent...）
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }

        // node.parent == null
        // node = node.parent.right
        return node.parent;
    }

    /**
     * 后继节点
     * 中序遍历时的后一个节点
     * 如果是二叉搜索树，后继节点就是最后一个比它大的节点
     *
     * @param node
     * @return
     */
    protected Node<E> successor(Node<E> node) {
        if (node == null) {
            return null;
        }

        Node<E> s = node.right;

        // 后继节点在右子树当中（node.right.left.left...）
        if (s != null) {
            while (s.left != null) {
                s = s.left;
            }

            return s;
        }

        // 从父节点、祖父节点中寻找（node.parent.parent.parent...）
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }

        return node.parent;
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node) node).right;
    }

    @Override
    public Object string(Object node) {
        // Node<E> myNode = (Node<E>)node;
        // String parentString = "null";

        // if (myNode.parent != null) {
        //     parentString = myNode.parent.element.toString();
        // }

        // return myNode.element + "_p(" + parentString + ")";

        return node;
    }
}
