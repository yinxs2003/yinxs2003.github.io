package com.code.note.listnode;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ListNodeUtil2 {
    private ListNode head;
    private ListNode h2;

    public void insertTail(int val) {

        ListNode node = new ListNode(val);

        if (head == null) {
            head = node;
            return;
        }

        ListNode t = head;
        while (head.next != null) {
            head = head.next;
        }
        head.next = node;
        head = t;
    }

    public void insertTail2(int val) {

        ListNode node = new ListNode(val);

        if (h2 == null) {
            h2 = node;
            return;
        }

        ListNode t = h2;
        while (h2.next != null) {
            h2 = h2.next;
        }
        h2.next = node;
        h2 = t;
    }

    public void print(ListNode x) {

        if (x == null) {
            return;
        }

        log.info("{}", x.val);
        while (x.next != null) {
            x = x.next;
            log.info("{}", x.val);
        }
    }

    /**
     * @param head
     * @return
     */
    List<Integer> list = new ArrayList<>();

    public ListNode reverseList(ListNode head) {

        if (head == null) {
            return null;
        }
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }

        log.info("{}", list);

        for (int i = list.size() - 1; i >= 0; i--) {
            insertTail2(list.get(i));
        }

        return h2;


    }

    public ListNode getHead() {
        return head;
    }

    public ListNode getHead2() {
        return h2;
    }

    public static void main(String[] args) {
        ListNodeUtil2 listNodeUtil = new ListNodeUtil2();
        listNodeUtil.insertTail(1);
        listNodeUtil.insertTail(2);
        listNodeUtil.insertTail(3);
        listNodeUtil.insertTail(4);

        ListNode h2 = listNodeUtil.reverseList(listNodeUtil.getHead());

        listNodeUtil.print(h2);


    }
}
