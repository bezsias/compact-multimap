package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

public interface BytePackager<T> {

    byte[] pack(byte[] bytes, List<T> values) throws IOException;

    byte[] pack(byte[] bytes, T value) throws IOException;

    byte[] init() throws IOException;

    ArrayList<T> unpack(byte[] bytes) throws IOException, ClassNotFoundException;
}
