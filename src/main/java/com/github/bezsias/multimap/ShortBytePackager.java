package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;

public class ShortBytePackager extends PrimitiveBytePackager<Short> {

    public ShortBytePackager(int blockSizeKb) throws IOException {
        super(blockSizeKb);
    }

    @Override
    protected void read(InputStream is, ArrayList<Short> list) throws IOException, ClassNotFoundException {
        try(DataInputStream ois = new DataInputStream(is)) {
            while (true) list.add(ois.readShort());
        } catch (EOFException e) {}
    }

    @Override
    public BytePack pack(BytePack pack, List<Short> values) throws IOException {
        baos.reset();
        baos.write(pack.noncompressed);
        if (values.isEmpty()) return pack;
        for (int i = 0, n = values.size(); i < n; i++) {
            dos.writeShort(values.get(i));
        }
        return flushStream(pack);
    }

    @Override
    public BytePack pack(BytePack pack, Short value) throws IOException {
        baos.reset();
        baos.write(pack.noncompressed);
        dos.writeShort(value);
        return flushStream(pack);
    }
}
