package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

public class IntBytePackager extends PrimitiveBytePackager<Integer> {

    public IntBytePackager(int blockSizeKb) throws IOException {
        super(blockSizeKb);
    }

    @Override
    public byte[] pack(byte[] bytes, List<Integer> values) throws IOException {
        if (values.isEmpty()) return bytes;
        for (int i = 0, n = values.size(); i < n; i++) {
            dos.writeInt(values.get(i));
        }
        return packStream(bytes);
    }

    @Override
    public byte[] pack(byte[] bytes, Integer value) throws IOException {
        dos.writeInt(value);
        return packStream(bytes);
    }

    @Override
    protected void read(InputStream is, ArrayList<Integer> list) throws IOException, ClassNotFoundException {
        try(DataInputStream ois = new DataInputStream(is)) {
            while (true) list.add(ois.readInt());
        } catch (EOFException e) {}
    }
}
