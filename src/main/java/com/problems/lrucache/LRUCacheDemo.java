package com.problems.lrucache;

public class LRUCacheDemo {

    public static void main(String[] args) {
        // Test with Integer keys and String values
        LRUCache<Integer, String> cache = new LRUCache<>(3);

        cache.put(1, "Value1");
        cache.put(2, "Value2");
        cache.put(3, "Value3");

        System.out.println("Get key 1: " + cache.get(1)); // Value1
        System.out.println("Get key 2: " + cache.get(2)); // Value2

        // Adding new element should evict key 3 (LRU)
        cache.put(4, "Value4");
        System.out.println("Get key 3: " + cache.get(3)); // null (evicted)
        System.out.println("Get key 4: " + cache.get(4)); // Value4

        // Test with String keys and Integer values
        System.out.println("\n--- Testing with String keys ---");
        LRUCache<String, Integer> stringCache = new LRUCache<>(2);

        stringCache.put("key1", 100);
        stringCache.put("key2", 200);
        System.out.println("Get key1: " + stringCache.get("key1")); // 100

        stringCache.put("key3", 300); // Evicts key2
        System.out.println("Get key2: " + stringCache.get("key2")); // null
        System.out.println("Get key3: " + stringCache.get("key3")); // 300
    }


}
