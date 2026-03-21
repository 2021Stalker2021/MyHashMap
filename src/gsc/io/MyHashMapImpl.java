package gsc.io;

public class MyHashMapImpl<K, V> implements MyHashMap<K, V> {
    private Node<K, V>[] buckets;
    private int capacity = 16;

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashMapImpl() {
        buckets = new Node[capacity];
    }

    private int getBucketIndex(K key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(K key, V value) {
        int index = getBucketIndex(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (current.key != null && current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
    }

    public V get(K key) {
        int index = getBucketIndex(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if ((current.key == null && key == null) ||
                    (current.key != null && current.key.equals(key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public void remove(K key) {
        int index = getBucketIndex(key);
        Node<K, V> current = buckets[index];
        Node<K, V> previous = null;


        while (current != null) {
            if ((current.key == null && key == null) ||
                    (current.key != null && current.key.equals(key))) {
                if (previous != null) {
                    previous.next = current.next;
                } else {
                    buckets[index] = current.next;
                }
                return;
            }
            previous = current;
            current = current.next;
        }
    }
}