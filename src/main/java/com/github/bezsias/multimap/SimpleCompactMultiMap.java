package com.github.bezsias.multimap;

import java.io.IOException;
import java.util.*;
import java.io.Serializable;

/**
 * Compact zipped byte[] based value serialization.
 * Works best for append only situation.
 */
public class SimpleCompactMultiMap<K, V extends Serializable> implements MultiMap<K, V> {

    private Map<K, BytePack> packs;
    private Map<K, byte[]> map;
    private int _size = 0;
    private SimpleBytePackager<V> packager;

    public static <K, V extends Serializable> MultiMap<K, V> objMultiMap(int blockSizeKb) throws IOException {
        return new SimpleCompactMultiMap<>(new ObjectSimpleBytePackager<>(blockSizeKb));
    }

    private SimpleCompactMultiMap(SimpleBytePackager<V> packager) throws IOException {
        this.packager = packager;
        this.map = new HashMap<>();
        this.packs = new HashMap<>();
    }

    public int memoryUsage() {
        int len = 0;
        for (K key: keys()) {
            BytePack pack = fetch(key);
            if (pack != null) len += pack.size();
        }
        return len;
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
        return map.containsKey(key) || packs.containsKey(key);
    }

    @Override
    public boolean contains(K key, V value) {
        List<V> values = get(key);
        return values.contains(value);
    }

    @Override
    public Set<K> keys() {
        Set<K> ks = new HashSet<>();
        ks.addAll(map.keySet());
        ks.addAll(packs.keySet());
        return ks;
    }

    private BytePack fetch(K key) {
        byte[] bytes = map.get(key);
        if (bytes == null) return packs.get(key);
        else return new BytePack(bytes);
    }

    private BytePack fetchOrDefault(K key) {
        BytePack pack = fetch(key);
        if (pack == null) return new BytePack();
        else return pack;
    }

    private void store(K key, BytePack pack) {
        if (pack.isCompressed()) {
            map.remove(key);
            packs.put(key, pack);
        } else {
            packs.remove(key);
            map.put(key, pack.noncompressed);
        }
    }

    @Override
    public void put(K key, V value) {
        try {
            BytePack pack = fetchOrDefault(key);
            pack = packager.pack(pack, value);
            store(key, pack);
            _size++;
        } catch (IOException e) {}
    }

    @Override
    public void putAll(K key, List<V> values) {
        if (values.isEmpty()) return;
        try {
            BytePack pack = fetchOrDefault(key);
            pack = packager.pack(pack, values);
            store(key, pack);
            _size += values.size();
        } catch (IOException e) {}
    }

    @Override
    public List<V> get(K key) {
        BytePack pack = fetch(key);
        if (pack == null){
            return Collections.emptyList();
        } else {
            try {
                return packager.unpack(pack);
            } catch (IOException | ClassNotFoundException e) {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public void remove(K key) {
        _size -= get(key).size(); //FIXME: too expensive?
        map.remove(key);
        packs.remove(key);
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
        packs.clear();
        _size = 0;
    }
}
