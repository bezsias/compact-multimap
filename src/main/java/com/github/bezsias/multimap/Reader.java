package com.github.bezsias.multimap;

import java.io.ObjectInputStream;
import java.io.DataInputStream;
import java.io.IOException;

interface Reader<T> {
    T read(ObjectInputStream is, DataInputStream dis) throws IOException, ClassNotFoundException;
}
