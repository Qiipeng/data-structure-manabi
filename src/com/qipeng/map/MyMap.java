package com.qipeng.map;

/**
 * 映射接口
 */
public interface MyMap<K, V> {

    int size();

    boolean isEmpty();

    void clear();

    /**
     * 添加
     *
     * @param key
     * @param value
     * @return 被覆盖的值
     */
    V put(K key, V value);

    /**
     * 根据键返回对应值
     *
     * @param key
     * @return
     */
    V get(K key);

    V remove(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    void traversal(Visitor<K, V> visitor);

    abstract class Visitor<K, V> {
        public boolean stop;

        public abstract boolean visit(K key, V value);
    }
}
