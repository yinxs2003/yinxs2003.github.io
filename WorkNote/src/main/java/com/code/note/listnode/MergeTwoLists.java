package com.code.note.listnode;

import com.sun.javafx.geom.Vec2d;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MergeTwoLists {

    ListNode head = new ListNode();

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode t = head;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                head.next = new ListNode(l1.val);
                head = head.next;
                l1 = l1.next;
            } else {
                head.next = new ListNode(l2.val);
                head = head.next;
                l2 = l2.next;
            }
        }

        if (l1 == null) {
            ListNode t1 = head;
            while (head.next != null) {
                head = head.next;
            }
            head.next = l2;
        }

        if (l2 == null) {
            ListNode t1 = head;
            while (head.next != null) {
                head = head.next;
            }
            head.next = l1;
        }

        return t.next;
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

    public static void main(String[] args) {

        int[] arr1 = {5};
        int[] arr2 = {1, 2, 4};
        ListNode l1 = null;
        ListNode l2 = null;

        ListNodeUtil listNodeUtil = new ListNodeUtil();
        for (int a : arr1) {
            listNodeUtil.insertTail(a);
        }

        ListNodeUtil listNodeUtil2 = new ListNodeUtil();
        for (int a : arr2) {
            listNodeUtil2.insertTail(a);
        }

//        listNodeUtil.print(listNodeUtil.getHead());
//        listNodeUtil2.print(listNodeUtil2.getHead());

        MergeTwoLists m = new MergeTwoLists();
        ListNode resultHead = m.mergeTwoLists(listNodeUtil.getHead(), listNodeUtil2.getHead());
        m.print(resultHead);

    }
}
