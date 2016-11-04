package com.github.bezsias.multimap;

import java.io.*;

abstract class SimplePrimitiveBytePackager<T extends Serializable> extends AbstractSimpleBytePackager<T> {
    DataOutputStream dos;

    SimplePrimitiveBytePackager(int blockSizeKb) throws IOException {
        super(blockSizeKb);
        dos = new DataOutputStream(baos);
    }

    BytePack flushStream(BytePack pack) throws IOException {
        dos.flush();
        return append(pack, baos.toByteArray());
    }
}
