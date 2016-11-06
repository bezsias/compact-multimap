package com.github.bezsias.multimap;

import java.io.*;

abstract class PrimitiveBytePackager<T extends Serializable> extends AbstractBytePackager<T> {
    DataOutputStream dos;

    PrimitiveBytePackager(int blockSizeKb) throws IOException {
        super(blockSizeKb);
        dos = new DataOutputStream(baos);
    }

    BytePack flushStream(BytePack pack) throws IOException {
        dos.flush();
        return append(pack, baos.toByteArray());
    }
}
