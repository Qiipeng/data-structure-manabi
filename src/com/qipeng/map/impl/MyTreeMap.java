package com.qipeng.map.impl;

import com.qipeng.map.MyMap;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 利用RBTree实现映射
 */
@SuppressWarnings({"DuplicatedCode", "JavaDoc"})
public class MyTreeMap<K, V> implements MyMap<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private int size;
    private Node<K, V> root;
    private Comparator<K> comparator;

    public MyTreeMap() {

    }

    public MyTreeMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    private static class Node<K, V> {
        public K key;
        public V value;

        public boolean color = RED;

        public Node<K, V> left;
        public Node<K, V> right;
        public Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
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
        public Node<K, V> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }

            return null;
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
        root = null;
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        keyNotNullCheck(key);

        // 添加第一个节点
        if (root == null) {
            root = new Node<>(key, value, null);
            black(root);
            size++;

            // 返回覆盖的值
            return null;
        }

        // 添加的不是第一个节点
        // 找到父节点
        Node<K, V> parent = root;
        Node<K, V> node = root;

        int compare = 0;

        while (node != null) {
            compare = compare(key, node.key);
            parent = node;

            if (compare > 0) {
                node = node.right;
            } else if (compare < 0) {
                node = node.left;
            } else {
                V oldValue = node.value;
                node.key = key;
                node.value = value;

                return oldValue;
            }
        }

        // 插入到父节点的左或右
        Node<K, V> newNode = new Node<>(key, value, parent);
        if (compare > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }

        size++;

        // 新添加节点之后的处理
        afterPut(newNode);

        return null;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);

        return node != null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    private V remove(Node<K, V> node) {
        if (node == null) {
            return null;
        }

        V oldValue = node.value;

        size--;

        if (node.withTwoLeaf()) { // 度为 2 的节点
            // 找到后继节点
            Node<K, V> s = successor(node);
            // 用后继节点的值覆盖度为 2 的节点的值
            node.key = s.key;
            node.value = s.value;
            // 删除后继节点
            node = s;
        }

        // 删除 node 节点(node 的度必然是 1 或者 0)
        Node<K, V> replacement = node.left != null ? node.left : node.right;

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

            // 真正被删除之后，对新生成的树做处理
            afterRemove(replacement);
        } else if (node.parent == null) { // node 是叶子节点并且是根节点
            root = null;
        } else { // node 是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {  // node == node.parent.right
                node.parent.right = null;
            }

            // 真正被删除之后，对新生成的树做处理
            afterRemove(node);
        }

        return oldValue;
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (root == null) {
            return false;
        }

        Queue<Node<K, V>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node<K, V> node = queue.poll();

            if (valEquals(value, node.value)) {
                return true;
            }

            if (node.left != null) {
                queue.offer(node.left);
            }

            if (node.right != null) {
                queue.offer(node.right);
            }
        }

        return false;
    }

    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    private boolean valEquals(V v1, V v2) {
        return v1 == null ? v2 == null : v1.equals(v2);
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) {
            return;
        }

        traversal(root, visitor);
    }

    private void traversal(Node<K, V> node, Visitor<K, V> visitor) {
        if (node == null || visitor.stop) {
            return;
        }

        traversal(node.left, visitor);

        if (visitor.stop) {
            return;
        }

        visitor.visit(node.key, node.value);

        traversal(node.right, visitor);
    }


    private void keyNotNullCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

    }

    private void afterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;

        // 添加的是根节点，或者上溢到了根节点
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色，直接返回
        if (isBlack(parent)) {
            return;
        }

        Node<K, V> uncle = parent.sibling(); // 叔父节点
        Node<K, V> grand = red(parent.parent); // 祖父节点

        // 叔父节点是红色(B 树节点上溢）
        if (isRed(uncle)) {
            black(parent);
            black(uncle);

            // 把祖父节点当做是新添加的节点
            afterPut(grand);
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

    private void afterRemove(Node<K, V> node) {
        // 如果删除的节点是红色
        // if (isRed(node)) { return; }

        // 如果删除的节点是红色
        // 或者用以取代 node 的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }

        Node<K, V> parent = node.parent;
        // 删除的是根节点
        if (parent == null) {
            return;
        }

        // 删除的是黑色叶子节点(下溢)
        // 判断被删除的 node 是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<K, V> sibling = left ? parent.right : parent.left;

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

    /**
     * 左旋转
     *
     * @param grand
     */
    private void rotateLeft(Node<K, V> grand) {
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left;
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
    private void rotateRight(Node<K, V> grand) {
        Node<K, V> parent = grand.left;
        Node<K, V> child = parent.right;
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
    private void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {
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
     * 比较两个元素的大小，避免硬编码
     *
     * @param key1
     * @param key2
     * @return 返回值 == 0，则 ele1 == ele2；
     * ******* 返回值 > 0，则 ele1 > ele2；
     * ******* 返回值 < 0，则 ele1 < ele2
     */
    private int compare(K key1, K key2) {
        // 当用户自定义比较器时，优先使用自定义规则
        if (comparator != null) {
            return this.comparator.compare(key1, key2);
        }

        // 当用户没有自定义比较器，却实现 comparable 接口时，使用改规则
        return ((Comparable<K>) key1).compareTo(key2);
    }

    /**
     * 染色操作
     *
     * @param node
     * @param color
     * @return
     */
    private Node<K, V> color(Node<K, V> node, boolean color) {
        if (node == null) {
            return null;
        }

        node.color = color;

        return node;
    }

    /**
     * 染成红色
     *
     * @param node
     * @return
     */
    private Node<K, V> red(Node<K, V> node) {
        return color(node, RED);
    }

    /**
     * 染成黑色
     *
     * @param node
     * @return
     */
    private Node<K, V> black(Node<K, V> node) {
        return color(node, BLACK);
    }

    /**
     * 节点颜色
     *
     * @param node
     * @return
     */
    private boolean colorOf(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }

    /**
     * 节点是否为黑色
     *
     * @param node
     * @return
     */
    private boolean isBlack(Node<K, V> node) {
        return colorOf(node) == BLACK;
    }

    /**
     * 节点是否为红色
     *
     * @param node
     * @return
     */
    private boolean isRed(Node<K, V> node) {
        return colorOf(node) == RED;
    }

    /**
     * 后继节点
     * 中序遍历时的后一个节点
     * 如果是二叉搜索树，后继节点就是最后一个比它大的节点
     *
     * @param node
     * @return
     */
    private Node<K, V> successor(Node<K, V> node) {
        if (node == null) {
            return null;
        }

        Node<K, V> s = node.right;

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

    private Node<K, V> node(K key) {
        Node<K, V> node = root;

        while (node != null) {
            int cmp = compare(key, node.key);

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
}
