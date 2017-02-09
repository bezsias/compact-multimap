package com.github.bezsias.multimap;

import java.util.*;
import java.io.*;

public class CompactByteArrayMultiMap<K> extends CompactMultiMap<K, byte[]> {

    CompactByteArrayMultiMap(BytePackager<byte[]> packager, MapFactory<K> mapFactory) throws IOException {
        super(packager, mapFactory);
    }

    protected int getIndex(List<byte[]> values, byte[] value) {
        for (int i = 0, n = values.size(); i < n ; i++) {
            if (Arrays.equals(values.get(i), value)) return i;
        }
        return -1;
    }

    @Override
    protected boolean remove(List<byte[]> values, byte[] value) {
        int idx = getIndex(values, value);
        if (idx == -1) return false;
        else {
            values.remove(idx);
            return true;
        }
    }

    @Override
    protected boolean contains(List<byte[]> values, byte[] value) {
        return getIndex(values, value) != -1;
    }

}
