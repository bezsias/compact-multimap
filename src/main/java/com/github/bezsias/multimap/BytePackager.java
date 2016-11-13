package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

public interface BytePackager<T> {

    BytePack pack(BytePack pack, List<T> values) throws IOException;

    BytePack pack(BytePack pack, T value) throws IOException;

    ArrayList<T> unpack(BytePack pack) throws IOException, ClassNotFoundException;

    static BytePackager<Boolean> booleanBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, (ois, dis) -> dis.readBoolean(), (value, oss, dos) -> dos.writeBoolean(value));
    }

    static BytePackager<Byte> byteBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, (ois, dis) -> dis.readByte(), (value, oss, dos) -> dos.writeByte(value));
    }

    static BytePackager<Short> shortBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, (ois, dis) -> dis.readShort(), (value, oss, dos) -> dos.writeShort(value));
    }

    static BytePackager<Integer> intBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, (ois, dis) -> dis.readInt(), (value, oss, dos) -> dos.writeInt(value));
    }

    static BytePackager<Long> longBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, (ois, dis) -> dis.readLong(), (value, oss, dos) -> dos.writeLong(value));
    }

    static BytePackager<Float> floatBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, (ois, dis) -> dis.readFloat(), (value, oss, dos) -> dos.writeFloat(value));
    }

    static BytePackager<Double> doubleBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, (ois, dis) -> dis.readDouble(), (value, oss, dos) -> dos.writeDouble(value));
    }

    static <T extends java.io.Serializable> BytePackager<T> objBytePackager(int blockSizeKb) throws IOException {
        return new BytePackagerImpl<>(blockSizeKb, (ois, dis) -> (T) ois.readObject(), (value, oss, dos) -> oss.writeObject(value));
    }

}
