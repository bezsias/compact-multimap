package com.github.bezsias.multimap;

import java.io.DataOutputStream;
import java.io.IOException;

interface Writer<T> {
    void write(T t, DataOutputStream os) throws IOException;
}
