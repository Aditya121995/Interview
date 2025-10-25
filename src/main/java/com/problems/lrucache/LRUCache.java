package com.problems.lrucache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K,V>> cacheMap;
    private final DoublyLinkedList<K,V> list;
    private final ReentrantLock lock;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new ConcurrentHashMap<>(capacity);
        this.list = new DoublyLinkedList<>();
        this.lock = new ReentrantLock();
    }

    public V get(K key) {
        lock.lock();
        try {
            if (!cacheMap.containsKey(key)) {
                return null;
            }

            Node<K,V> node = cacheMap.get(key);
            list.moveToFront(node);
            return node.getValue();
        } finally {
            lock.unlock();
        }
    }

    public void put(K key, V value) {
        lock.lock();
        try {
            if (cacheMap.containsKey(key)) {
                Node<K,V> node = cacheMap.get(key);
                node.setValue(value);
                list.moveToFront(node);
            } else {
                Node<K,V> node = new Node<>(key, value);
                cacheMap.put(key, node);
                list.addToFront(node);

                if (cacheMap.size() > capacity) {
                    Node<K,V> removedNode = list.removeLast();
                    if (removedNode != null) {
                        cacheMap.remove(removedNode.getKey());
                    }
                }
            }
        } finally {
            lock.unlock();
        }

    }

    public void remove(K key) {
        lock.lock();
        try {
            if (cacheMap.containsKey(key)) {
                Node<K,V> removedNode = cacheMap.get(key);
                list.removeNode(removedNode);
                cacheMap.remove(key);
            }
        } finally {
            lock.unlock();
        }
    }
}
