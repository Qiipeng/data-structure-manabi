package com.qipeng.map;

import com.qipeng.map.impl.MyTreeMap;

public class TestMyMap {

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        MyMap<String, Integer> map = new MyTreeMap<>();
        map.put("class", 2);
        map.put("public", 5);
        map.put("text", 6);
        map.put("public", 8);

        map.traversal(new MyMap.Visitor<String, Integer>() {
            @Override
            public boolean visit(String key, Integer value) {
                System.out.println(key + " - " + value);

                return false;
            }
        });
    }
}
