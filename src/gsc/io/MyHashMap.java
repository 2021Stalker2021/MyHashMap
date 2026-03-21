package gsc.io;

public interface MyHashMap<K,V> {
    void put(K key, V value);
    V get(K key);
    void remove(K key);
}
