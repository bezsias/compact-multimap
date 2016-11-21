package com.github.bezsias.multimap;

public class MapInfo {

    public static <K> int memoryUsage(CompactMultiMap<K, ?> map) {
        int len = 0;
        for (K key: map.keys()) {
            len += map.fetch(key).size();
        }
        return len;
    }

}
