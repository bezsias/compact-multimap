package com.github.bezsias.multimap;

import java.util.Map;

public interface MapFactory<K> {
    Map<K, byte[]> createMap();
}

