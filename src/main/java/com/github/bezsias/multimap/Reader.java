package com.github.bezsias.multimap;

import java.io.ObjectInputStream;
import java.io.IOException;

interface Reader<T> {
    T read(ObjectInputStream is) throws IOException, ClassNotFoundException;
}
