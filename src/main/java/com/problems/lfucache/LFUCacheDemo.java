package com.problems.lfucache;

public class LFUCacheDemo {
    public static void main(String[] args) {
        LFUCache<Integer, Integer> cache = new LFUCache<>(2);

        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println("get(1): " + cache.get(1));        // 1, freq of key 1 = 2

        cache.put(3, 3);                                       // Evicts key 2 (freq=1, LRU)
        System.out.println("get(2): " + cache.get(2));        // null (evicted)

        cache.put(4, 4);                                       // Evicts key 3 (freq=1)
        System.out.println("get(3): " + cache.get(3));        // null (evicted)
        System.out.println("get(4): " + cache.get(4));        // 4, freq of key 4 = 2

        cache.put(5, 5);                                       // Evicts key 1 (freq=2 but LRU)
        System.out.println("get(1): " + cache.get(1));        // null (evicted)
        System.out.println("get(4): " + cache.get(4));        // 4
        System.out.println("get(5): " + cache.get(5));        // 5
    }

}
