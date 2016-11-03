package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class BytePackager<T extends Serializable> extends AbstractBytePackager<T> {

    private class OIS extends ObjectInputStream {

        OIS(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected void readStreamHeader() throws IOException {
        }
    }

    private ObjectOutputStream oos;

    public BytePackager(int blockSizeKb) throws IOException {
        super(blockSizeKb);
        this.oos = new ObjectOutputStream(baos);
        baos.reset(); // clear header
    }

    @Override
    public byte[] pack(byte[] bytes, List<T> values) throws IOException {
        if (values.isEmpty()) return bytes;
        for (T value: values) {
            oos.writeObject(value);
        }
        oos.reset();
        oos.flush();
        byte[] serialized = baos.toByteArray();
        baos.reset();
        return pack(bytes, serialized);
    }

    @Override
    public byte[] pack(byte[] bytes, T value) throws IOException {
        oos.writeObject(value);
        oos.reset();
        oos.flush();
        byte[] serialized = baos.toByteArray();
        baos.reset();
        return pack(bytes, serialized);
    }



    @Override
    protected void read(InputStream is, ArrayList<T> list) throws IOException, ClassNotFoundException {
        try(OIS ois = new OIS(is)) {
            while (true) {
                T obj = (T) ois.readObject();
                list.add(obj);
            }
        } catch (EOFException e) {}
    }

}
