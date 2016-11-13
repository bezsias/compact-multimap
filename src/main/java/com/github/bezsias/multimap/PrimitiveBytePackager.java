package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

public class PrimitiveBytePackager<T extends Serializable> extends AbstractBytePackager<T> {
    DataOutputStream dos;
    Writer<T> writer;
    Reader<T> reader;

    public static BytePackager<Short> shortBytePackager(int blockSizeKb) throws IOException {
        return new PrimitiveBytePackager<>(blockSizeKb, DataInputStream::readShort, (value, os) -> os.writeShort(value));
    }

    public static BytePackager<Integer> intBytePackager(int blockSizeKb) throws IOException {
        return new PrimitiveBytePackager<>(blockSizeKb, DataInputStream::readInt, (value, os) -> os.writeInt(value));
    }

    PrimitiveBytePackager(int blockSizeKb, Reader<T> reader, Writer<T> writer) throws IOException {
        super(blockSizeKb);
        dos = new DataOutputStream(baos);
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    protected void read(InputStream is, ArrayList<T> list) throws IOException, ClassNotFoundException {
        try(DataInputStream ois = new DataInputStream(is)) {
            while (true) list.add(reader.read(ois));
        } catch (EOFException e) {}
    }

    @Override
    public BytePack pack(BytePack pack, List<T> values) throws IOException {
        baos.reset();
        baos.write(pack.noncompressed);
        if (values.isEmpty()) return pack;
        for (int i = 0, n = values.size(); i < n; i++) {
            writer.write(values.get(i), dos);
        }
        return flushStream(pack);
    }

    @Override
    public BytePack pack(BytePack pack, T value) throws IOException {
        baos.reset();
        baos.write(pack.noncompressed);
        writer.write(value, dos);
        return flushStream(pack);
    }

    BytePack flushStream(BytePack pack) throws IOException {
        dos.flush();
        return append(pack, baos.toByteArray());
    }
}
