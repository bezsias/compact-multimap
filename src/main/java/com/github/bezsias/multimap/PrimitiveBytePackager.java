package com.github.bezsias.multimap;

import java.io.*;

abstract class PrimitiveBytePackager<T extends Serializable> extends AbstractBytePackager<T> {
    DataOutputStream dos;

    PrimitiveBytePackager(int blockSizeKb) throws IOException {
        super(blockSizeKb);
        dos = new DataOutputStream(baos);
    }

    byte[] packStream(byte[] bytes) throws IOException {
        dos.flush();
        byte[] serialized = baos.toByteArray();
        baos.reset();
        return pack(bytes, serialized);
    }
}
