package com.problems.lrucache;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Node<K, V> {
    private K key;
    @Setter
    private V value;
    @Setter
    private Node<K, V> previous;
    @Setter
    private Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
        this.previous = null;
        this.next = null;
    }
}
