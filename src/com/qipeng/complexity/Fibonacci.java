package com.qipeng.complexity;

import com.qipeng.utils.TimeTool;

/**
 * 复杂度相关
 */
public class Fibonacci {

    /*
     * 斐波那契数列
     * 0 1 1 2 3 5 8 13 ...
     */

    /**
     * 求第 n 项（方法 1）
     * 利用递归性能不好
     *
     * @param n 第 n 项
     * @return 第 n 项值
     */
    public static int fib1(int n) {
        if ((n <= 1)) {
            return n;
        }

        return fib1(n - 1) + fib1(n - 2);
    }

    /**
     * 求第 n 项（方法 2）
     * 改进性能问题
     *
     * @param n 第 n 项
     * @return 第 n 项值
     */
    public static int fib2(int n) {
        if ((n <= 1)) {
            return n;
        }

        int first = 0;
        int second = 1;
        for (int i = 0; i < n - 1; i++) {
            int sum = first + second;
            first = second;
            second = sum;
        }

        return second;
    }

    public static void main(String[] args) {
        int n = 4;

        TimeTool.check("fib1", new TimeTool.Task() {
            @Override
            public void execute() {
                System.out.println(fib1(n));
            }
        });

       TimeTool.check("fib2", () -> System.out.println(fib2(n)));

    }
}
