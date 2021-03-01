package com.code.note.listnode;

/**
 * @author : yxs1112003
 * @created : 2021-02-26
 **/
public class HashCircle {

    public boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }

        while (head != null && head.next != null) {
            ListNode n1 = head.next;
            ListNode n2 = head.next.next;
            if (n1 == n2) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Hello world");
    }
}
