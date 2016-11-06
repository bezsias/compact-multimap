package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

public class IntBytePackager extends PrimitiveBytePackager<Integer> {

    public IntBytePackager(int blockSizeKb) throws IOException {
        super(blockSizeKb);
    }

    @Override
    protected void read(InputStream is, ArrayList<Integer> list) throws IOException, ClassNotFoundException {
        try(DataInputStream ois = new DataInputStream(is)) {
            while (true) list.add(ois.readInt());
        } catch (EOFException e) {}
    }

    @Override
    public BytePack pack(BytePack pack, List<Integer> values) throws IOException {
        baos.reset();
        baos.write(pack.noncompressed);
        if (values.isEmpty()) return pack;
        for (int i = 0, n = values.size(); i < n; i++) {
            dos.writeInt(values.get(i));
        }
        return flushStream(pack);
    }

    @Override
    public BytePack pack(BytePack pack, Integer value) throws IOException {
        baos.reset();
        baos.write(pack.noncompressed);
        dos.writeInt(value);
        return flushStream(pack);
    }
}
