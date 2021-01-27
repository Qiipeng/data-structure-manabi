package com.qipeng.tree.binaryTree;

import java.util.Comparator;

/**
 * 二叉树搜索--必须具备可比较性
 * 重构
 */
@SuppressWarnings({"Duplicates", "unchecked", "JavaDoc"})
public class MyBST<E> extends MyBinaryTree<E> {

    private Comparator<E> comparator;

    public MyBST() {

    }

    public MyBST(Comparator<E> comparator) {
        this.comparator = comparator;
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
            root = createNode(element, null);
            size++;

            // 新添加节点之后的处理
            afterAdd(root);

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
        Node<E> newNode = createNode(element, parent);
        if (compare > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }

        size++;

        // 新添加节点之后的处理
        afterAdd(newNode);
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

            // 真正被删除之后，对新生成的树做处理
            afterRemove(replacement);
        } else if (node.parent == null) { // node 是叶子节点并且是根节点
            root = null;

            // 真正被删除之后，对新生成的树做处理
            afterRemove(node);
        } else { // node 是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {  // node == node.parent.right
                node.parent.right = null;
            }

            // 真正被删除之后，对新生成的树做处理
            afterRemove(node);
        }
    }

    /**
     * 添加 node 之后的调整
     *
     * @param node 新添加的节点
     */
    protected void afterAdd(Node<E> node) {

    }

    /**
     * 删除 node 之后的调整
     *
     * @param node 被删除的节点，或者用以取代被删除节点的子节点
     */
    protected void afterRemove(Node<E> node) {

    }

    /**
     * 删除 node 之后的调整
     *
     // * @param node 被删除的节点
     // * @param replacement 被删除的节点
     */
    /*
    protected void afterRemove(Node<E> node, Node<E> replacement) {

    }
    */

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
