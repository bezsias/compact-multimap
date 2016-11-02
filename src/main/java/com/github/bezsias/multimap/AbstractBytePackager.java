package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;
import java.util.zip.*;

abstract class AbstractBytePackager<T extends Serializable> {

    public abstract byte[] pack(byte[] bytes, List<T> values) throws IOException;

    public abstract byte[] pack(byte[] bytes, T value) throws IOException;

    public abstract ArrayList<T> unpack(byte[] bytes) throws IOException, ClassNotFoundException;

    protected abstract void read(InputStream is, ArrayList<T> list) throws IOException, ClassNotFoundException;

    static int OFFSET_SPACE = 2; // first 2 byte is the size of uncompressed data
    private byte[] OFFSET_BYTES = new byte[]{ Util.ZERO_BYTE, Util.ZERO_BYTE };

    private int blockSize;
    ByteArrayOutputStream baos;

    AbstractBytePackager(int blockSizeKb) throws IOException {
        this.baos = new ByteArrayOutputStream();
        this.blockSize = Math.min(blockSizeKb, 64) * 1024;
    }

    public byte[] init() throws IOException {
        return OFFSET_BYTES;
    }


    byte[] pack(byte[] bytes, byte[] serialized) throws IOException {
        if (bytes.length < 2) throw new IllegalArgumentException("byte array is not initialized");
        short rawLength = Util.readShort(bytes, 0);
        int length = rawLength + serialized.length;
        int compressedOffset = OFFSET_SPACE + rawLength;
        boolean hasCompressedData = compressedOffset < bytes.length;
        if (length >= blockSize) {
            if (hasCompressedData) {
                byte[] uncompressedBytes = unzip(bytes, compressedOffset, bytes.length - compressedOffset);
                byte[] result = new byte[uncompressedBytes.length + rawLength + serialized.length];
                System.arraycopy(uncompressedBytes, 0, result, 0, uncompressedBytes.length);
                System.arraycopy(bytes, OFFSET_SPACE, result, uncompressedBytes.length, rawLength);
                System.arraycopy(serialized, 0, result, uncompressedBytes.length + rawLength, serialized.length);
                baos.write(OFFSET_BYTES);
                return zip(result);
            } else {

                byte[] result = new byte[rawLength + serialized.length];
                System.arraycopy(bytes, OFFSET_SPACE, result, 0, rawLength);
                System.arraycopy(serialized, 0, result, rawLength, serialized.length);
                baos.write(OFFSET_BYTES);
                return zip(result);
            }
        } else { // store as it is
            byte[] result = new byte[bytes.length + serialized.length];

            Util.writeShort((short) length, result, 0);
            // copy existing uncompressed data
            System.arraycopy(bytes, OFFSET_SPACE, result, OFFSET_SPACE, rawLength);
            // copy new uncompressed data
            System.arraycopy(serialized, 0, result, compressedOffset, serialized.length);
            // copy existing compressed data
            if (hasCompressedData) {
                System.arraycopy(bytes, compressedOffset, result, compressedOffset + serialized.length, bytes.length - compressedOffset);
            }
            return result;
        }
    }

    private byte[] zip(byte[] bytes) throws IOException {
        GZIPOutputStream gzos = new GZIPOutputStream(baos, true);
        gzos.write(bytes, 0, bytes.length);
        gzos.finish();
        gzos.flush();
        gzos.close();
        byte[] compressed = baos.toByteArray();
        baos.reset();
        return compressed;

    }

    private byte[] unzip(byte[] bytes, int offset, int length) throws IOException {
        GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(bytes, offset, length));
        Util.copy(gzis, baos);
        gzis.close();
        baos.flush();
        byte[] uncompressed = baos.toByteArray();
        baos.reset();
        return uncompressed;
    }
}
