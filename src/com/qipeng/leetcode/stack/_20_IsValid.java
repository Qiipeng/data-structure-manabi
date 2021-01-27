package com.qipeng.leetcode.stack;

import java.util.Stack;

/**
 * https://leetcode-cn.com/problems/valid-parentheses/
 */
public class _20_IsValid {


    /**
     * 有效括号
     *
     * @param str
     * @return
     */
    public boolean isValid(String str) {
        // 1）遇见左字符，入栈
        // 2）遇见右字符，与左字符比较
        // 3）所有字符扫描结束，若栈为空--true
        //                  若栈不为空--false

        Stack<Character> stack = new Stack<>();
        int length = str.length();

        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);

            // 遇到左字符
            if (c == '{' || c == '[' || c == '(') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }

                char left = stack.pop();

                if (left == '(' && c != ')') {
                    return false;
                }
                if (left == '[' && c != ']') {
                    return false;
                }
                if (left == '{' && c != '}') {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public boolean isVlid_(String str) {
        while (str.contains("{}") || str.contains("[]") || str.contains("()")) {
            str.replace("{}", "");
            str.replace("[]", "");
            str.replace("()", "");
        }

        return str.isEmpty();
    }
}
