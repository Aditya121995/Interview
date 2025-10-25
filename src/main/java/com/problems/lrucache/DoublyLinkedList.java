package com.problems.lrucache;

import lombok.Getter;

@Getter
public class DoublyLinkedList<K, V> {
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public DoublyLinkedList() {
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        this.head.setNext(this.tail);
        this.tail.setPrevious(this.head);
    }

    public void addToFront(Node<K, V> node) {
        Node<K, V> nextNode = head.getNext();
        head.setNext(node);
        node.setPrevious(head);
        node.setNext(nextNode);
        nextNode.setPrevious(node);
    }

    public void removeNode(Node<K, V> node) {
        Node<K, V> prevNode = node.getPrevious();
        Node<K, V> nextNode = node.getNext();

        prevNode.setNext(nextNode);
        nextNode.setPrevious(prevNode);
    }

    public void moveToFront(Node<K, V> node) {
        removeNode(node);
        addToFront(node);
    }

    public Node<K, V> removeLast() {
        if (head.getNext() == tail) {
            return null;
        }
        Node<K, V> node = tail.getPrevious();
        removeNode(node);
        return node;
    }

    public boolean isEmpty() {
        return head.getNext() == tail;
    }

}
