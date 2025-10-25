package com.problems.lfucache;

import lombok.ToString;

public class DoublyLinkedList<K, V> {
    private final Node<K,V> head;
    private final Node<K,V> tail;

    public DoublyLinkedList() {
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        this.head.setNext(tail);
        this.tail.setPrevious(head);
    }

    public void addToFront(Node<K, V> node) {
        Node<K,V> nextNode = head.getNext();
        head.setNext(node);
        node.setPrevious(head);
        node.setNext(nextNode);
        nextNode.setPrevious(node);
    }

    public void removeNode(Node<K,V> node) {
        Node<K,V> prev = node.getPrevious();
        Node<K,V> next = node.getNext();

        prev.setNext(next);
        next.setPrevious(prev);
    }

    public Node<K,V> removeLast() {
        if (this.head.getNext() == this.tail) {
            return null;
        }
        Node<K,V> node = tail.getPrevious();
        removeNode(node);
        return node;
    }

    public boolean isEmpty() {
        return this.head.getNext() == this.tail;
    }
}
