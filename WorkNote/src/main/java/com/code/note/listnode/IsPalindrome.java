package com.code.note.listnode;

import java.util.ArrayList;
import java.util.List;

public class IsPalindrome {
    List<Integer> list = new ArrayList<>();

    public boolean isPalindrome(ListNode head) {

        while (head != null) {
            list.add(head.val);
            head = head.next;
        }

        while (!list.isEmpty()) {
            if (list.size() == 1) {
                return true;
            }

            if (list.get(0).equals(list.get(list.size() - 1))) {
                list.remove(0);
                list.remove(list.size() - 1);
            } else {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        ListNodeUtil listNodeUtil = new ListNodeUtil();
        listNodeUtil.insertTail(1);
        listNodeUtil.insertTail(2);

        listNodeUtil.print(listNodeUtil.getHead());
        IsPalindrome isPalindrome = new IsPalindrome();
        boolean a = isPalindrome.isPalindrome(listNodeUtil.getHead());
        System.out.println(a);
    }
}
