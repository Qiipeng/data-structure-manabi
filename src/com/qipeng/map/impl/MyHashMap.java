package com.qipeng.map.impl;

import com.qipeng.map.MyMap;
import com.qipeng.utils.binaryTreePrinter.BinaryTreeInfo;
import com.qipeng.utils.binaryTreePrinter.printer.BinaryTrees;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 利用HashTable实现映射
 * 数据使用RBTree节点
 */
@SuppressWarnings({"unchecked", "JavaDoc", "DuplicatedCode", "rawtypes"})
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private int size;
    private Node<K, V>[] table; // 存放索引、数据（每一棵红黑树根节点）
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f; // 默认装填因子

    protected static class Node<K, V> {
        K key;
        V value;
        int hashCode;
        boolean color = RED;

        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            int hashCode = key == null ? 0 : key.hashCode();
            this.hashCode = hashCode ^ (hashCode >>> 16);
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean withTwoLeaf() {
            return left != null && right != null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        public Node<K, V> sibling() {
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
            return "Node_" + key + "_" + value;
        }
    }

    public MyHashMap() {
        // 左移4位 = 2^4
        table = new Node[DEFAULT_CAPACITY];
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
        if (size == 0) {
            return;
        }

        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }

        size = 0;
    }

    @Override
    public V put(K key, V value) {
        resize();

        // 先计算出索引
        int index = index(key);
        // 是否有红黑树根节点
        Node<K, V> root = table[index];

        // 根节点处理
        if (root == null) {
            root = createNode(key, value, null);
            table[index] = root;
            size++;

            fixAfterPut(root);

            return null;
        }

        // 非根节点，哈希冲突（添加新的节点到红黑树上）
        Node<K, V> parent;
        Node<K, V> node = root;

        int ret = 0;
        K k1 = key;
        int h1 = hash(k1);
        Node<K, V> finder;
        // 是否已经搜索过这个key
        boolean searched = false;

        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hashCode;

            if (h1 > h2) {
                ret = 1;
            } else if (h1 < h2) {
                ret = -1;
            } else if (Objects.equals(k1, k2)) {
                ret = 0;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (((Comparable) k1).compareTo(k2)) != 0) {
                ret = ((Comparable) k1).compareTo(k2);
            } else if (searched) { // 已经扫描过
                ret = System.identityHashCode(k1) - System.identityHashCode(k2);
            } else { // 还未扫描过，即先扫描然后再根据内存地址大小决定左右
                if ((node.left != null && (finder = node(node.left, k1)) != null)
                        || (node.right != null && (finder = node(node.right, k1)) != null)) {
                    // 已经存在这个key
                    node = finder;
                    ret = 0;
                } else { // 不存在这个key
                    searched = true;
                    ret = System.identityHashCode(k1) - System.identityHashCode(k2);
                }
            }

            if (ret > 0) {
                node = node.right;
            } else if (ret < 0) {
                node = node.left;
            } else {
                V oldValue = node.value;
                node.key = key;
                node.value = value;

                return oldValue;
            }
        } while (node != null);

        // 插入到父节点的左或右
        Node<K, V> newNode = createNode(key, value, parent);
        if (ret > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }

        size++;

        // 新添加节点之后的处理
        fixAfterPut(newNode);

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

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (size == 0) {
            return false;
        }

        Queue<Node<K, V>> queue = new LinkedList<>();

        for (Node<K, V> kvNode : table) {
            if (kvNode == null) {
                continue;
            }

            queue.offer(kvNode);

            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();

                if (Objects.equals(value, node.value)) {
                    return true;
                }

                if (node.left != null) {
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }

        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || visitor == null) {
            return;
        }

        Queue<Node<K, V>> queue = new LinkedList<>();

        for (Node<K, V> kvNode : table) {
            if (kvNode == null) {
                continue;
            }

            queue.offer(kvNode);

            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();

                if (visitor.visit(node.key, node.value)) {
                    return;
                }

                if (node.left != null) {
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
    }

    public void print() {
        if (size == 0) {
            return;
        }

        for (int i = 0; i < table.length; i++) {
            System.out.println("index = " + i);
            Node<K, V> root = table[i];
            BinaryTrees.println(new BinaryTreeInfo() {
                @Override
                public Object root() {
                    return root;
                }

                @Override
                public Object left(Object node) {
                    return ((Node<K, V>) node).left;
                }

                @Override
                public Object right(Object node) {
                    return ((Node<K, V>) node).right;
                }

                @Override
                public Object string(Object node) {
                    return node;
                }
            });

            System.out.println("---------------------------------------------------");
        }
    }

    /**
     * 根据key生成对应的索引（在桶数组中对应的位置）
     *
     * @param key
     * @return
     */
    private int index(K key) {
        if (key == null) return 0;

        return hash(key) & (table.length - 1);
    }

    /**
     * 扰动计算
     *
     * @param key
     * @return
     */
    private int hash(K key) {
        if (key == null) return 0;

        int hashCode = key.hashCode();
        // 高低16位混合运算出另一个hash值
        return hashCode ^ (hashCode >>> 16);
    }

    private int index(Node<K, V> node) {
        return node.hashCode & (table.length - 1);
    }

    /**
     * 根据对应key找到节点
     *
     * @param key
     * @return
     */
    private Node<K, V> node(K key) {
        Node<K, V> root = table[index(key)];

        return root == null ? null : node(root, key);
    }

    /**
     * 从根节点找到对应节点
     *
     * @param node 根节点
     * @param k1
     * @return
     */
    private Node<K, V> node(Node<K, V> node, K k1) {
        int h1 = hash(k1);
        Node<K, V> finder;
        int ret;

        while (node != null) {
            K k2 = node.key;
            int h2 = node.hashCode;

            // 先比较哈希值
            if (h1 > h2) {
                node = node.right;
            } else if (h1 < h2) {
                node = node.left;
            } else if (Objects.equals(k1, k2)) {
                return node;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (ret = ((Comparable) k1).compareTo(k2)) != 0) {
                node = ret > 0 ? node.right : node.left;
            } else if (node.right != null
                    && (finder = node(node.right, k1)) != null) {
                return finder;
            } else { // 只能向左边寻找
                node = node.left;
            }
        }

        return null;
    }

    /**
     * 父类开放接口允许在put方法中使用LinkedNode类型
     *
     * @return
     */
    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {

        return new Node(key, value, parent);
    }

    /**
     * 处理删除节点
     *
     * @param replacedNode 原本要删除的节点
     * @param removeNode   真正被删除的节点
     */
    protected void afterRemove(Node<K, V> replacedNode, Node<K, V> removeNode) {

    }

    /**
     * 扩容（当数据量大于容量的0.75时进行扩容）
     */
    private void resize() {
        // 装填因子 <= 0.75
        if ((float) size / table.length <= DEFAULT_LOAD_FACTOR) {
            return;
        }

        // 旧数据
        Node<K, V>[] oldTable = table;

        // 扩容为原来的2倍
        table = new Node[oldTable.length << 1];

        // 转移旧数据
        Queue<Node<K, V>> queue = new LinkedList<>();

        for (Node<K, V> kvNode : oldTable) {
            if (kvNode == null) {
                continue;
            }

            queue.offer(kvNode);

            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();

                if (node.left != null) {
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    queue.offer(node.right);
                }

                // 挪动代码放到最后
                moveNode(node);
            }
        }
    }

    private void moveNode(Node<K, V> newNode) {
        // 重置
        newNode.parent = null;
        newNode.left = null;
        newNode.right = null;
        newNode.color = RED;

        // 先计算出索引
        int index = index(newNode);
        // 是否有红黑树根节点
        Node<K, V> root = table[index];

        // 根节点处理
        if (root == null) {
            root = newNode;
            table[index] = root;

            fixAfterPut(root);

            return;
        }

        // 非根节点，哈希冲突（添加新的节点到红黑树上）
        Node<K, V> parent;
        Node<K, V> node = root;

        int ret;
        K k1 = newNode.key;
        int h1 = hash(k1);

        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hashCode;

            if (h1 > h2) {
                ret = 1;
            } else if (h1 < h2) {
                ret = -1;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && ((Comparable) k1).compareTo(k2) != 0) {
                ret = ((Comparable) k1).compareTo(k2);
            } else {
                ret = System.identityHashCode(k1) - System.identityHashCode(k2);
            }

            if (ret > 0) {
                node = node.right;
            } else if (ret < 0) {
                node = node.left;
            }
        } while (node != null);

        // 插入到父节点的左或右
        newNode.parent = parent;

        if (ret > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }

        // 新添加节点之后的处理
        fixAfterPut(newNode);
    }

    protected V remove(Node<K, V> node) {
        if (node == null) {
            return null;
        }

        Node<K, V> replacedNode = node;
        V oldValue = node.value;

        size--;

        if (node.withTwoLeaf()) { // 度为 2 的节点
            // 找到后继节点
            Node<K, V> s = successor(node);
            // 用后继节点的值覆盖度为 2 的节点的值
            node.key = s.key;
            node.value = s.value;
            node.hashCode = s.hashCode;
            // 删除后继节点
            node = s;
        }

        // 删除 node 节点(node 的度必然是 1 或者 0)
        Node<K, V> replacement = node.left != null ? node.left : node.right;
        int index = index(node);

        if (replacement != null) { // node 是度为 1 的节点
            // 更改 parent
            replacement.parent = node.parent;

            // 更改 parent 的 left、right 的指向
            if (node.parent == null) { // node 是度为 1 的节点并且是根节点
                table[index] = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else { // node == node.parent.right
                node.parent.right = replacement;
            }

            // 真正被删除之后，对新生成的树做处理
            fixAfterRemove(replacement);
        } else if (node.parent == null) { // node 是叶子节点并且是根节点
            table[index] = null;
        } else { // node 是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {  // node == node.parent.right
                node.parent.right = null;
            }

            // 真正被删除之后，对新生成的树做处理
            fixAfterRemove(node);
        }

        // 交给子类处理
        afterRemove(replacedNode, node);

        return oldValue;
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


    private void fixAfterPut(Node<K, V> node) {
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
            fixAfterPut(grand);
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

    private void fixAfterRemove(Node<K, V> node) {
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
                    fixAfterRemove(parent);
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
                    fixAfterRemove(parent);
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
            table[index(grand)] = parent;
        }

        // 更新 child 的 parent
        if (child != null) {
            child.parent = grand;
        }

        // 更新 grand 的 parent
        grand.parent = parent;
    }

    /**
     * 比较key的大小
     *
     * @param k1
     * @param k2
     * @param h1 k1的hashCode
     * @param h2 k2的hashCode
     * @return
     */
    private int compare(K k1, K k2, int h1, int h2) {
        // 比较哈希值
        int result = h1 - h2;

        if (result != 0) {
            return result;
        }

        // 比较equals
        if (Objects.equals(k1, k2)) {
            return 0;
        }

        // 哈希值相等，但equals不等
        // if ((k1 != null && k2 != null)) {
        //     // 比较类名
        //     String k1ClassName = k1.getClass().getName();
        //     String k2ClassName = k2.getClass().getName();
        //
        //     result = k1ClassName.compareTo(k2ClassName);
        //
        //     if (result != 0) {
        //         return result;
        //     }
        //
        //     // 同一种类型并且具备可比较性
        //     if (k1 instanceof Comparable) {
        //         return ((Comparable) k1).compareTo(k2);
        //     }
        // }

        if (k1 != null && k2 != null
                && k1.getClass() == k2.getClass()
                && k1 instanceof Comparable) {
            // 同一种类型并且具备可比较性
            return ((Comparable) k1).compareTo(k2);
        }

        // 同一种类型、哈希值相等，但是不具备可比较性
        // k1不为null，k2为null
        // k1为null，k2不为null
        // 比较内存地址
        return System.identityHashCode(k1) - System.identityHashCode(k2);
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
}
