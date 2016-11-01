package jmultimap;

import java.util.*;

/**
 * Map<K, List<V>>
 * Duplicate K-V pairs are allowed.
 * Values are ordered for a single key.
 */
public interface MultiMap<K, V> {

    /** Returns the number of key-value pairs. */
    int size();

    boolean isEmpty();

    boolean contains(K key);

    boolean contains(K key, V value);

    Set<K> keys();

    void put(K key, V value);

    void putAll(K key, List<V> values);

    List<V> get(K key);

    /** Removes all values associated with key. */
    void remove(K key);

    /** Returns the count of removed elements */
    void remove(K key, V value);

    void clear();
}
