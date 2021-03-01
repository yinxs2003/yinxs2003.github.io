package com.code.note.listnode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListNodeUtil {
    private ListNode head;

    public void insertTail(int val) {
        if (head == null) {
            head = new ListNode(val);
            return;
        }

        ListNode t = head;
        while (head.next != null) {
            head = head.next;
        }
        head.next = new ListNode(val);
        head = t;
    }

    public void print(ListNode head) {
        while (head != null) {
            log.info("{}", head.val);
            head = head.next;
        }
    }

    public ListNode getHead() {
        return head;
    }
}
