package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

public interface BytePackager<T> {

    BytePack pack(BytePack pack, List<T> values) throws IOException;

    BytePack pack(BytePack pack, T value) throws IOException;

    ArrayList<T> unpack(BytePack pack) throws IOException, ClassNotFoundException;

    static BytePackager<Short> shortBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, ObjectInputStream::readShort, (value, os) -> os.writeShort(value));
    }

    static BytePackager<Integer> intBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, ObjectInputStream::readInt, (value, os) -> os.writeInt(value));
    }

    static <T extends java.io.Serializable> BytePackager<T> objBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, is -> (T) is.readObject(), (value, os) -> os.writeObject(value));
    }

}
