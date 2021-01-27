package com.qipeng.leetcode.linkedList;


/**
 * https://leetcode-cn.com/problems/reverse-linked-list/
 */
public class _206_ReverseList {

    /**
     * 利用递归
     *
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        if ((head == null || head.next == null)) {
            return head;
        }

        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;

        return newHead;
    }

    /**
     * 利用循环
     *
     * @param head
     * @return
     */
    public ListNode reverseList2(ListNode head) {
        if ((head == null || head.next == null)) {
            return head;
        }

        ListNode newHead = null;

        while ((head != null)) {
            ListNode temp = head.next;
            head.next = newHead;
            newHead = head;
            head = temp;
        }

        return newHead;
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


}
