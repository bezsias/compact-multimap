package com.github.bezsias.multimap;

import java.io.DataInputStream;
import java.io.IOException;

interface Reader<T> {
    T read(DataInputStream is) throws IOException;
}
