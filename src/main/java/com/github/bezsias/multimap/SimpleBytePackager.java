package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public interface SimpleBytePackager<T> {

    BytePack pack(BytePack pack, List<T> values) throws IOException;

    BytePack pack(BytePack pack, T value) throws IOException;

    ArrayList<T> unpack(BytePack pack) throws IOException, ClassNotFoundException;
}
