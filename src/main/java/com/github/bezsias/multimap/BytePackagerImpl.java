package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

class BytePackagerImpl<T extends Serializable> extends AbstractBytePackager<T> {
    private ObjectOutputStream oos;
    private DataOutputStream dos;
    private Writer<T> writer;
    private Reader<T> reader;

    private class OIS extends ObjectInputStream {

        OIS(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected void readStreamHeader() throws IOException {
        }
    }

    BytePackagerImpl(int blockSizeKb, Reader<T> reader, Writer<T> writer) throws IOException {
        super(blockSizeKb);
        this.reader = reader;
        this.writer = writer;
        this.oos = new ObjectOutputStream(baos);
        this.dos = new DataOutputStream(baos);
        baos.reset(); // clear header
    }

    @Override
    protected void read(InputStream is, ArrayList<T> list) throws IOException, ClassNotFoundException {
        try(OIS ois = new OIS(is); DataInputStream dis = new DataInputStream(is)) {
            while (true) list.add(reader.read(ois, dis));
        } catch (EOFException e) {}
    }

    @Override
    public BytePack pack(BytePack pack, List<T> values) throws IOException {
        if (values.isEmpty()) return pack;
        baos.reset();
        baos.write(pack.noncompressed);
        for (int i = 0, n = values.size(); i < n; i++) {
            writer.write(values.get(i), oos, dos);
        }
        return flushStream(pack);
    }

    @Override
    public BytePack pack(BytePack pack, T value) throws IOException {
        baos.reset();
        baos.write(pack.noncompressed);
        writer.write(value, oos, dos);
        return flushStream(pack);
    }

    private BytePack flushStream(BytePack pack) throws IOException {
        oos.flush();
        dos.flush();
        BytePack bytePack = append(pack, baos.toByteArray());
        if (bytePack.justCompressed()) oos.reset(); // reset stream after a block compress
        return bytePack;
    }
}
