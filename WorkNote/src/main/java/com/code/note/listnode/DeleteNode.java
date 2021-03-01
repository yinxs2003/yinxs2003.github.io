package com.code.note.listnode;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DeleteNode {
    private ListNode head;

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

    public void print(ListNode x) {
        while (x != null) {
            System.out.println(x.val);
            x = x.next;
        }
    }

    public ListNode deleteNode(ListNode head, int val) {
        ListNode t = head;

        if (head.val == val) {
            head =head.next;
            return head;
        }

        while (true) {

            if (head.next.val == val) {
                head.next = head.next.next;
                return t;
            }

            head = head.next;
        }
    }


    public ListNode getHead() {
        return head;
    }

    public static void main(String[] args) {
        DeleteNode listNodeUtil = new DeleteNode();
        listNodeUtil.insertTail(4);
        listNodeUtil.insertTail(5);
        listNodeUtil.insertTail(6);

        listNodeUtil.insertTail(7);

        listNodeUtil.insertTail(1);
        listNodeUtil.insertTail(9);
//        listNodeUtil.print(listNodeUtil.getHead());
        ListNode root = listNodeUtil.deleteNode(listNodeUtil.getHead(), 4);
        listNodeUtil.print(root);
    }
}
