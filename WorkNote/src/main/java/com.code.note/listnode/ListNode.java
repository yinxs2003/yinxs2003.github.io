package com.code.note.listnode;

import lombok.extern.slf4j.Slf4j;

/**
 * 单链表
 *
 * @param <T>
 */

@Slf4j
public class ListNode<T> {

    private int size;
    private Node head;

    public class Node {
        T value;
        Node next;

        Node(T value) {
            this.value = value;
        }
    }

    public int size() {
        return size;
    }

    public void addFromHead(T value) {
        Node n = new Node(value);
        size++;

        if (head == null) {
            head = n;
            return;
        }

        n.next = head;
        head = n;

        return;
    }

    public void addToTail(T value) {
        Node n = new Node(value);
        size++;

        if (head == null) {
            head = n;
            return;
        }

        Node x = head;
        while (x.next != null) {
            x = x.next;
        }
        x.next = n;


        /**
         // 这里哪里不对？

         Node x1 = head;
         while (x1 != null) {
         x1 = x1.next;
         }
         x1 = n;

         **/
    }

    // 遍历
    public void traverse() {
        if (head == null) return;
        Node x = head;
        while (x != null) {
            log.info("{}", x.value);
            x = x.next;
        }
    }

    public static void main(String[] args) {
        log.info("hello");
        ListNode<Integer> listNode = new ListNode<>();
        listNode.addToTail(1);
        listNode.addToTail(2);
        listNode.addToTail(3);
        listNode.addToTail(4);
        listNode.addToTail(5);
        log.info("{}", listNode.size());
        listNode.traverse();
    }


}

