package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

/**
 * Compact zipped byte[] based value serialization.
 * Works best for append only situation.
 */
public class CompactMultiMap<K, V extends Serializable> implements MultiMap<K, V> {

    private Map<K, byte[]> map;
    private int _size = 0;
    private BytePackager<V> packager;

    public CompactMultiMap() throws java.io.IOException {
        this(1);
    }

    public CompactMultiMap(int blockSizeKb) throws java.io.IOException {
        this.packager = new BytePackager<>(blockSizeKb);
        this.map = new HashMap<>();
    }

    @Override
    public int size() {
        return _size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public boolean contains(K key, V value) {
        List<V> values = get(key);
        return values.contains(value);
    }

    @Override
    public Set<K> keys() {
        return map.keySet();
    }

    @Override
    public void put(K key, V value) {
        try {
            byte[] bytes = map.getOrDefault(key, packager.init());
            bytes = packager.pack(bytes, value);
            map.put(key, bytes);
            _size++;
        } catch (java.io.IOException e) {}
    }

    @Override
    public void putAll(K key, List<V> values) {
        if (values.isEmpty()) return;
        try {
            byte[] bytes = map.getOrDefault(key, packager.init());
            bytes = packager.pack(bytes, values);
            map.put(key, bytes);
            _size += values.size();
        } catch (java.io.IOException e) {}
    }

    @Override
    public List<V> get(K key) {
        byte[] bytes = map.get(key);
        if (bytes == null){
            return Collections.emptyList();
        } else {
            try {
                return packager.unpack(bytes);
            } catch (IOException | ClassNotFoundException e) {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public void remove(K key) {
        _size -= get(key).size(); //FIXME: too expensive?
        map.remove(key);
    }

    @Override
    public void remove(K key, V value) {
        List<V> values = get(key);
        while (values.remove(value));
        remove(key);
        putAll(key, values);
    }

    @Override
    public void clear() {
        map.clear();
        _size = 0;
    }
}
