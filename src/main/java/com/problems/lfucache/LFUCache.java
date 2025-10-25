package com.problems.lfucache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LFUCache<K,V> {
    private final int capacity;
    private final Map<K, Node<K,V>> cacheMap;
    private final Map<Integer, DoublyLinkedList<K,V>> freqMap;
    private final ReentrantLock lock;
    private int minFreq;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new ConcurrentHashMap<>();
        this.freqMap = new ConcurrentHashMap<>();
        this.minFreq = 0;
        this.lock = new ReentrantLock();
    }

    public V get(K key) {
        lock.lock();
        try {
            if (!cacheMap.containsKey(key)) {
                return null;
            }

            Node<K,V> node = cacheMap.get(key);
            updateFrequency(node);
            return node.getValue();
        } finally {
            lock.unlock();
        }
    }

    public void put(K key, V value) {
        lock.lock();
        try {
            if (cacheMap.containsKey(key)) {
                Node<K,V> oldNode = cacheMap.get(key);
                oldNode.setValue(value);
                updateFrequency(oldNode);
            } else {
                while (cacheMap.size() >= capacity) {
                    evictLFU();
                }

                Node<K,V> newNode = new Node<>(key, value);
                cacheMap.put(key, newNode);
                this.minFreq = 1;
                addNodeToFrequencyList(newNode);

            }
        } finally {
            lock.unlock();
        }
    }

    private void evictLFU() {
        DoublyLinkedList<K,V> minFreqList = freqMap.get(this.minFreq);
        if (minFreqList == null) {
            return;
        }

        Node<K, V> removedNode = minFreqList.removeLast();
        if (removedNode != null) {
            cacheMap.remove(removedNode.getKey());
        }

        if (minFreqList.isEmpty()) {
            freqMap.remove(this.minFreq);
        }

    }

    private void updateFrequency(Node<K, V> node) {
        int oldFreq = node.getFrequency();
        int newFreq = oldFreq + 1;

        DoublyLinkedList<K,V> oldList = freqMap.get(oldFreq);
        oldList.removeNode(node);

        if (oldList.isEmpty() && oldFreq == minFreq) {
            minFreq = newFreq;
            freqMap.remove(oldFreq);
        }

        node.setFrequency(newFreq);
        addNodeToFrequencyList(node);
    }

    private void addNodeToFrequencyList(Node<K, V> node) {
        if (!freqMap.containsKey(node.getFrequency())) {
            freqMap.put(node.getFrequency(), new DoublyLinkedList<K,V>());
        }

        DoublyLinkedList<K,V> list = freqMap.get(node.getFrequency());
        list.addToFront(node);
    }
}
