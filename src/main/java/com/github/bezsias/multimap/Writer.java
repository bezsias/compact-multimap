package com.github.bezsias.multimap;

import java.io.ObjectOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

interface Writer<T> {
    void write(T t, ObjectOutputStream os, DataOutputStream dos) throws IOException;
}
