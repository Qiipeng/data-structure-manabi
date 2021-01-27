package com.qipeng.hashTable;

import com.qipeng.map.MyMap;
import com.qipeng.map.impl.MyHashMap;
import com.qipeng.map.impl.MyLinkedHashMap;

import java.util.HashMap;
import java.util.Map;

public class TestMyHashTable {

    public static void main(String[] args) {
        testMyHashMap();
    }

    // 字符串的哈希值计算s
    public static void hashCodeForString() {
        String str = "jack";
        int len = str.length();
        int hashCode = 0;

        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            // 31是一个奇素数，JVM会将31 * 1优化成(i << 5)- i
            // hashCode = (hashCode << 5) - hashCode + c;
            hashCode = hashCode * 31 + c;
        }

        // hashCode = ((j * 31 + a) *31 + c) * 31 + k
        System.out.println(hashCode);
    }

    // hashCode总结
    public static void hashCodeReview() {
        int a = 110;
        float b = 10.6f;
        long c = 156L;
        double d = 10.9;
        String e = "rose";

        System.out.println(Integer.hashCode(a));
        System.out.println(Float.hashCode(b));
        System.out.println(Long.hashCode(c));
        System.out.println(Double.hashCode(d));
        System.out.println(e.hashCode());
    }

    // 自定义对象的hashCode
    public static void hashCodeForObject() {
        Person person1 = new Person(10, 1.67f, "jack");
        Person person2 = new Person(10, 1.67f, "jack");

        Map<Object, Object> map = new HashMap<>();
        map.put(person1, "abc");
        map.put("test", "ccc");
        map.put(person2, "bcd");

        System.out.println(map.size());
        System.out.println(map.get(person1));
    }

    public static void testMyHashMap() {
        MyHashMap<Object, Integer> map = new MyLinkedHashMap<>();

        for (int i = 0; i < 20; i++) {
            map.put(new Item(i), i);
        }

        map.put(new Item(4), 100);
        map.print();
        map.remove(new Item(8));

        map.traversal(new MyMap.Visitor<Object, Integer>() {
            @Override
            public boolean visit(Object key, Integer value) {
                System.out.println(key + " - " + value);

                return false;
            }
        });

        map.print();
    }
}
