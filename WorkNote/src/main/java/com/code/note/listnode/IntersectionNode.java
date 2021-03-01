package com.code.note.listnode;

import java.util.ArrayList;
import java.util.List;

public class IntersectionNode {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }

        int aLen = getLength(headA);
        int bLen = getLength(headB);

        boolean flag = aLen > bLen;
        int len = Math.abs(aLen - bLen);


        if (flag) { // A大
            int i = 0;
            while (i < len) {
                headA = headA.next;
                i++;
            }

        } else { // B大
            int i = 0;
            while (i < len) {
                headB = headB.next;
                i++;
            }
        }

        System.out.println(headA.val);
        System.out.println(headB.val);

        while (headA != null && headB != null) {

            if (headA == headB) {
                return headA;
            }

            headA = headA.next;
            headB = headB.next;
        }
        return null;
    }

    private int getLength(ListNode headA) {
        int aLen = 0;
        while (headA != null) {
            aLen++;
            headA = headA.next;
        }
        return aLen;
    }

    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
