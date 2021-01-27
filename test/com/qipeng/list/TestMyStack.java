package com.qipeng.list;

import com.qipeng.linearList.stack.MyStack;


public class TestMyStack {

    public static void main(String[] args) {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);

        System.out.println(stack.peek());

        System.out.println(stack);
    }
}
