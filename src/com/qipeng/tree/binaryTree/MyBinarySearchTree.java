package com.qipeng.tree.binaryTree;


import com.qipeng.utils.binaryTreePrinter.BinaryTreeInfo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;


/**
 * 二叉树搜索--必须具备可比较性
 */
@SuppressWarnings({"Duplicates", "JavaDoc", "unused", "unchecked", "rawtypes"})
public class MyBinarySearchTree<E> implements BinaryTreeInfo {

    private int size; // 节点数
    private Node<E> root; // 根节点
    private Comparator<E> comparator;

    public MyBinarySearchTree() {

    }

    public MyBinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * 成员变量用来存储元素
     *
     * @param <E>
     */
    private static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;

        Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        /**
         * 是否是叶子节点
         *
         * @return
         */
        private boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * 是否有两个子节点
         *
         * @return
         */
        private boolean withTwoLeaf() {
            return left != null && right != null;
        }
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
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 是否包含某个元素
     *
     * @param element
     * @return
     */
    public boolean contains(E element) {
        return node(element) != null;
    }

    /**
     * 添加元素
     *
     * @param element
     */
    public void add(E element) {
        elementNotNullCheck(element);

        // 添加第一个节点
        if (root == null) {
            root = new Node<>(element, null);
            size++;
            return;
        }

        // 添加的不是第一个节点
        // 找到父节点
        Node<E> parent = root;
        Node<E> node = root;

        int compare = 0;

        while (node != null) {
            compare = compare(element, node.element);
            parent = node;

            if (compare > 0) {
                node = node.right;
            } else if (compare < 0) {
                node = node.left;
            } else {
                node.element = element;

                return;
            }
        }

        // 插入到父节点的左或右
        if (compare > 0) {
            parent.right = new Node<>(element, parent);
        } else {
            parent.left = new Node<>(element, parent);
        }

        size++;
    }

    /**
     * 删除元素
     * 删除节点-叶子节点
     * 直接删除
     * node == node.parent.left
     * node.parent.left = null
     * <p>
     * node == node.parent.right
     * node.parent.right = null
     * <p>
     * node.parent == null
     * root = null
     * <p>
     * 删除节点-度为 1 的节点
     * 用子节点替代原节点的位置
     * child 是 node.left 或者 child 是 node.right
     * <p>
     * 用 child 替代 node 的位置
     * 如果 node 是左子节点
     * child.parent = node.parent
     * node.parent.left = child
     * <p>
     * 如果 node 是右子节点
     * child.parent = node.parent
     * node.parent.right = child
     * <p>
     * 如果 node 是根节点
     * root = child
     * child.parent = null
     * <p>
     * 删除节点-度为 2 的节点
     * 先用前驱或者后继节点的值覆盖原节点的值
     * 然后删除对应的前驱或者后继节点
     * <p>
     * 如果一个节点的度为 2，那么
     * 它的前驱、后继节点的度只可能是 1 和 0
     */
    public void remove(E element) {
        remove(node(element));
    }

    private void remove(Node<E> node) {
        if (node == null) {
            return;
        }

        size--;

        if (node.withTwoLeaf()) { // 度为 2 的节点
            // 找到后继节点
            Node<E> s = successor(node);
            // 用后继节点的值覆盖度为 2 的节点的值
            node.element = s.element;
            // 删除后继节点
            node = s;
        }

        // 删除 node 节点(node 的度必然是 1 或者 0)
        Node<E> replacement = node.left != null ? node.left : node.right;

        if (replacement != null) { // node 是度为 1 的节点
            // 更改 parent
            replacement.parent = node.parent;

            // 更改 parent 的 left、right 的指向
            if (node.parent == null) { // node 是度为 1 的节点并且是根节点
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else { // node == node.parent.right
                node.parent.right = replacement;
            }
        } else if (node.parent == null) { // node 是叶子节点并且是根节点
            root = null;
        } else { // node 是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {  // node == node.parent.right
                node.parent.right = null;
            }
        }
    }

    private Node<E> node(E element) {
        Node<E> node = root;

        while (node != null) {
            int cmp = compare(element, node.element);

            if (cmp == 0) {
                return node;
            }

            if (cmp > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        return null;
    }

    /**
     * 清空树
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * 该接口让外界自己设计遍历树的方法
     *
     * @param <E>
     */
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
     * 检查元素是否为空
     */
    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element should not be null");
        }
    }

    /**
     * 比较两个元素的大小，避免硬编码
     *
     * @param ele1
     * @param ele2
     * @return 返回值 == 0，则 ele1 == ele2；
     * ******* 返回值 > 0，则 ele1 > ele2；
     * ******* 返回值 < 0，则 ele1 < ele2
     */
    private int compare(E ele1, E ele2) {
        // 当用户自定义比较器时，优先使用自定义规则
        if (comparator != null) {
            return this.comparator.compare(ele1, ele2);
        }

        // 当用户没有自定义比较器，却实现 comparable 接口时，使用改规则
        return ((Comparable<E>) ele1).compareTo(ele2);
    }

    /**
     * 前驱节点
     * 中序遍历时的前一个节点
     * 如果是二叉搜索树，前驱节点就是前一个比它小的节点
     *
     * @param node
     * @return
     */
    private Node<E> predecessor(Node<E> node) {
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
    private Node<E> successor(Node<E> node) {
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
        return ((Node) node).element;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(root, sb, "");

        return sb.toString();
    }

    /**
     * @param node
     * @param sb
     * @param prefix 表示层级关系的前缀
     */
    private void toString(Node<E> node, StringBuilder sb, String prefix) {
        if (node == null) {
            return;
        }

        sb.append(node.element).append("\n");
        toString(node.left, sb, "L--");
        toString(node.right, sb, "R--");
    }
}
