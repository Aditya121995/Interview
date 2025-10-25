package com.problems.lfucache;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Node<K, V> {
    private final K key;
    @Setter
    private V value;
    @Setter
    private int frequency;
    @Setter
    private Node<K, V> previous;
    @Setter
    private Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
        this.previous=null;
        this.next=null;
        this.frequency=1;
    }
}
