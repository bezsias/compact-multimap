package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;
import java.util.zip.*;

abstract class AbstractSimpleBytePackager<T extends Serializable> implements SimpleBytePackager<T>{

    private static int LENGTH_SPACE = 2; // first 2 byte is the size of compressed data
    private static byte[] LENGTH_BYTES = new byte[]{ Util.ZERO_BYTE, Util.ZERO_BYTE };

    private int blockSize;
    ByteArrayOutputStream baos;

    AbstractSimpleBytePackager(int blockSizeKb) throws IOException {
        this.blockSize = blockSizeKb * 1024;
        this.baos = new ByteArrayOutputStream(blockSize);
    }

    BytePack append(BytePack pack, byte[] bytes) throws IOException {
        if (bytes.length >= blockSize) { // create a new compressed block
            baos.reset();

            baos.write(LENGTH_BYTES); // preserve space for length
            GZIPOutputStream gzos = new GZIPOutputStream(baos);
            gzos.write(bytes);
            gzos.close();

            byte[] compressed = baos.toByteArray();
            Util.writeShort(compressed.length - LENGTH_SPACE, compressed, 0);
            pack.appendCompressed(compressed);
        } else {
            pack.noncompressed = bytes;
        }
        return pack;
    }

    abstract protected void read(InputStream is, ArrayList<T> list) throws IOException, ClassNotFoundException;

    public ArrayList<T> unpack(BytePack pack) throws IOException, ClassNotFoundException {
        ArrayList<T> list = new ArrayList<>();
        if (pack.isCompressed()) {
            int offset = 0;
            while (offset < pack.compressed.length) { // read all compressed blocks
                int blockLength = Util.readShort(pack.compressed, offset);
                read(new GZIPInputStream(new ByteArrayInputStream(pack.compressed, offset + LENGTH_SPACE, blockLength)), list);
                offset += blockLength + LENGTH_SPACE;
            }
        }
        read(new ByteArrayInputStream(pack.noncompressed), list);
        return list;
    }
}
