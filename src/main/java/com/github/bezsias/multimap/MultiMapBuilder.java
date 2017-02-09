package com.github.bezsias.multimap;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class MultiMapBuilder<K> {

    private MapFactory<K> mapFactory = HashMap::new;
    private int blockSizeKb = 8;

    public MultiMapBuilder() {}

    public MultiMapBuilder<K> blockSizeKb(int blockSizeKb) {
        this.blockSizeKb = blockSizeKb;
        return this;
    }

    public MultiMapBuilder<K> mapFactory(MapFactory<K> mapFactory) {
        this.mapFactory = mapFactory;
        return this;
    }

    public MultiMap<K, Boolean> booleanMap() throws IOException {
        return new CompactMultiMap<>(BytePackager.booleanBytePackager(blockSizeKb), mapFactory);
    }

    public MultiMap<K, Byte> byteMap() throws IOException {
        return new CompactMultiMap<>(BytePackager.byteBytePackager(blockSizeKb), mapFactory);
    }

    public MultiMap<K, Short> shortMap() throws IOException {
        return new CompactMultiMap<>(BytePackager.shortBytePackager(blockSizeKb), mapFactory);
    }

    public MultiMap<K, Integer> intMap() throws IOException {
        return new CompactMultiMap<>(BytePackager.intBytePackager(blockSizeKb), mapFactory);
    }

    public MultiMap<K, Long> longMap() throws IOException {
        return new CompactMultiMap<>(BytePackager.longBytePackager(blockSizeKb), mapFactory);
    }

    public MultiMap<K, Float> floatMap() throws IOException {
        return new CompactMultiMap<>(BytePackager.floatBytePackager(blockSizeKb), mapFactory);
    }

    public MultiMap<K, Double> doubleMap() throws IOException {
        return new CompactMultiMap<>(BytePackager.doubleBytePackager(blockSizeKb), mapFactory);
    }

    public <V extends Serializable> MultiMap<K, V> objectMap() throws IOException {
        return new CompactMultiMap<>(BytePackager.objBytePackager(blockSizeKb), mapFactory);
    }

    public MultiMap<K, byte[]> byteArrayMap() throws IOException {
        return new CompactByteArrayMultiMap<>(BytePackager.byteArrayBytePackager(blockSizeKb), mapFactory);
    }
}


