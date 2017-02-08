package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;
import java.io.Serializable;

/**
 * Compact zipped byte[] based value serialization.
 * Works best for append only situation.
 */
public class CompactMultiMap<K, V extends Serializable> implements MultiMap<K, V> {

    private Map<K, byte[]> compressedMap;
    private Map<K, byte[]> noncompressedMap;
    private int _size = 0;
    private BytePackager<V> packager;

    CompactMultiMap(BytePackager<V> packager, MapFactory<K> mapFactory) throws IOException {
        this.packager = packager;
        this.noncompressedMap = mapFactory.createMap();
        this.compressedMap = mapFactory.createMap();
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
        return noncompressedMap.containsKey(key) || compressedMap.containsKey(key);
    }

    @Override
    public boolean contains(K key, V value) {
        List<V> values = get(key);
        return values.contains(value);
    }

    @Override
    public Set<K> keys() {
        Set<K> ks = new HashSet<>();
        ks.addAll(noncompressedMap.keySet());
        ks.addAll(compressedMap.keySet());
        return ks;
    }

    BytePack fetch(K key) {
        return new BytePack(noncompressedMap.get(key), compressedMap.get(key));
    }

    private void store(K key, BytePack pack) {
        if (pack.noncompressed != null) {
            noncompressedMap.put(key, pack.noncompressed);
        } else {
            noncompressedMap.remove(key);
        }

        if (pack.compressed != null) {
            compressedMap.put(key, pack.compressed);
        } else {
            compressedMap.remove(key);
        }
    }

    @Override
    public void put(K key, V value) {
        try {
            BytePack pack = fetch(key);
            pack = packager.pack(pack, value);
            store(key, pack);
            _size++;
        } catch (IOException e) {}
    }

    @Override
    public void putAll(K key, List<V> values) {
        if (values.isEmpty()) return;
        try {
            BytePack pack = fetch(key);
            pack = packager.pack(pack, values);
            store(key, pack);
            _size += values.size();
        } catch (IOException e) {}
    }

    @Override
    public List<V> get(K key) {
        BytePack pack = fetch(key);
        try {
            return packager.unpack(pack);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void remove(K key) {
        _size -= get(key).size();
        noncompressedMap.remove(key);
        compressedMap.remove(key);
    }

    @Override
    public void remove(K key, V value) {
        List<V> values = get(key);
        _size -= values.size();
        noncompressedMap.remove(key);
        compressedMap.remove(key);
        while (values.remove(value));
        putAll(key, values);
    }

    @Override
    public void clear() {
        noncompressedMap.clear();
        compressedMap.clear();
        _size = 0;
    }
}
