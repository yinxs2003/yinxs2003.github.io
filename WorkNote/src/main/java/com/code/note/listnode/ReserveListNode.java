package com.code.note.listnode;

public class ReserveListNode {
    public ListNode reverseList(ListNode head) {
        return reverseList(head, null);
    }

    public ListNode reverseList(ListNode head, ListNode newHead) {
        if (head == null) {
            return newHead;
        }

        ListNode oldNext = head.next;
        head.next = newHead;
        newHead = head;

        return reverseList(oldNext, newHead);
    }

    public static void main(String[] args) {
        ListNodeUtil listNodeUtil = new ListNodeUtil();
        listNodeUtil.insertTail(1);
        listNodeUtil.insertTail(2);
        listNodeUtil.insertTail(3);
        listNodeUtil.insertTail(4);
        listNodeUtil.insertTail(5);

        ReserveListNode reserveListNode = new ReserveListNode();
        ListNode newHead = reserveListNode.reverseList(listNodeUtil.getHead());
        listNodeUtil.print(newHead);
    }
}
