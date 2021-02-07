package com.code.note;

import com.code.note.tree.BTreePrinter;
import com.code.note.tree.Node;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {

    public Node buildMax(int[] arr) {
        return buildMax(arr, 0, arr.length);
    }

    public Node buildMax(int[] arr, int l, int r) {
        if (l > r) {
            return null;
        }
        int xIndex = getMaxIndex(arr, l, r);
        Node x = new Node(arr[xIndex]);
        x.left = buildMax(arr, l, xIndex - 1);
        x.right = buildMax(arr, xIndex + 1, r);
        return x;
    }

    public int getMaxIndex(int[] arr, int l, int r) {
        int maxIndex = l;
        int maxValue = arr[l];
        for (int i = l; i < r; i++) {
            if (maxValue < arr[i]) {
                maxValue = arr[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static void main(String[] args) {
        Test test = new Test();
        int[] arr = new int[] { 3, 2, 1, 6, 0, 5 };
        Node root = test.buildMax(arr, 0, arr.length - 1);
        // log.info("{}", root);
        BTreePrinter.printNode(root);
    }
}
