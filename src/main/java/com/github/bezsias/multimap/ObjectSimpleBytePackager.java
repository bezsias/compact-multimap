package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

public class ObjectSimpleBytePackager<T extends Serializable> extends AbstractSimpleBytePackager<T> {

    private class OIS extends ObjectInputStream {

        OIS(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected void readStreamHeader() throws IOException {
        }
    }

    private ObjectOutputStream oos;

    ObjectSimpleBytePackager(int blockSizeKb) throws IOException {
        super(blockSizeKb, true);
        this.oos = new ObjectOutputStream(baos);
        baos.reset(); // clear header
    }

    public BytePack pack(BytePack pack, T value) throws IOException {
        baos.reset();
        baos.write(pack.noncompressed);
        oos.writeObject(value);
        oos.flush();
        oos.reset();
        return append(pack, baos.toByteArray());
    }

    public BytePack pack(BytePack pack, List<T> values) throws IOException {
        baos.reset();
        baos.write(pack.noncompressed);
        for (int i = 0, n = values.size(); i < n; i++) {
            oos.writeObject(values.get(i));
        }
        oos.flush();
        oos.reset();
        return append(pack, baos.toByteArray());
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
