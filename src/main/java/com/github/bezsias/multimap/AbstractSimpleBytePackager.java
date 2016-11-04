package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;
import java.util.zip.*;

abstract class AbstractSimpleBytePackager<T extends Serializable> implements SimpleBytePackager<T>{

    private static int LENGTH_SPACE = 2; // first 2 byte is the size of compressed data
    private static byte[] LENGTH_BYTES = new byte[]{ Util.ZERO_BYTE, Util.ZERO_BYTE };

    private int blockSize;
    private boolean blockCompressed;
    ByteArrayOutputStream baos;

    AbstractSimpleBytePackager(int blockSizeKb, boolean blockCompressed) throws IOException {
        this.blockSize = blockSizeKb * 1024;
        this.baos = new ByteArrayOutputStream(blockSize);
        this.blockCompressed = blockCompressed;
    }


    public BytePack append(BytePack pack, byte[] bytes) throws IOException {
        if (bytes.length >= blockSize) { // create a new compressed block
            baos.reset();

            //FIXME: unzip for not block compressed
            baos.write(LENGTH_BYTES); // preserve space for length
            java.util.zip.GZIPOutputStream gzos = new java.util.zip.GZIPOutputStream(baos);
            gzos.write(bytes);
            gzos.close();

            byte[] compressed = baos.toByteArray();
            pack.appendCompressed(compressed);
            baos.reset();
        } else {
            pack.noncompressed = bytes;
        }
        return pack;
    }

    abstract protected void read(InputStream is, ArrayList<T> list) throws IOException, ClassNotFoundException;

    public ArrayList<T> unpack(BytePack pack) throws IOException, ClassNotFoundException {
        ArrayList<T> list = new ArrayList<>();
        if (blockCompressed) {
            int offset = 0;
            while (offset < pack.compressed.length) { // read all compressed blocks
                int blockLength = Util.readShort(pack.compressed, offset);
                read(new GZIPInputStream(new ByteArrayInputStream(pack.compressed, offset + LENGTH_SPACE, blockLength)), list);
                offset += blockLength + LENGTH_SPACE;
            }
            read(new ByteArrayInputStream(pack.noncompressed), list);
        } else {
            read(new GZIPInputStream(new ByteArrayInputStream(pack.compressed)), list);
            read(new ByteArrayInputStream(pack.noncompressed), list);
        }
        return list;
    }
}
