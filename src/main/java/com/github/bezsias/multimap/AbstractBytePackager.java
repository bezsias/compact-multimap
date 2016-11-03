package com.github.bezsias.multimap;

import java.io.*;
import java.util.*;
import java.util.zip.*;

abstract class AbstractBytePackager<T extends Serializable> implements BytePackager<T> {

    public abstract byte[] pack(byte[] bytes, List<T> values) throws IOException;

    public abstract byte[] pack(byte[] bytes, T value) throws IOException;

    protected abstract void read(InputStream is, ArrayList<T> list) throws IOException, ClassNotFoundException;

    private static int OFFSET_SPACE = 2; // first 2 byte is the size of uncompressed data
    private byte[] OFFSET_BYTES = new byte[]{ Util.ZERO_BYTE, Util.ZERO_BYTE };

    private int blockSize;
    ByteArrayOutputStream baos;

    AbstractBytePackager(int blockSizeKb) throws IOException {
        this.baos = new ByteArrayOutputStream(64 * 1024);
        this.blockSize = Math.min(blockSizeKb, 64) * 1024;
    }

    public byte[] init() throws IOException {
        return OFFSET_BYTES;
    }

    byte[] pack(byte[] bytes, byte[] serialized) throws IOException {
        if (bytes.length < 2) throw new IllegalArgumentException("byte array is not initialized");
        int rawLength = uncompressedLength(bytes);
        int length = rawLength + serialized.length;
        if (length >= blockSize) { // create a new compressed block

            int offset = uncompressedLengthOffset(bytes) - rawLength;
            baos.reset();
            baos.write(bytes, 0, offset);
            baos.write(OFFSET_BYTES); // preserve space for length

            GZIPOutputStream gzos = new GZIPOutputStream(baos);
            gzos.write(bytes, offset, rawLength);
            gzos.write(serialized);
            gzos.close();
            baos.write(OFFSET_BYTES);
            byte[] result = baos.toByteArray();

            int compressedLength = result.length - offset - (2 * OFFSET_SPACE);

            Util.writeShort(compressedLength, result, offset);
            baos.reset();
            return result;

        } else { // store as it is
            byte[] result = new byte[bytes.length + serialized.length];
            int pos = uncompressedLengthOffset(bytes);
            System.arraycopy(bytes, 0, result, 0, pos);
            System.arraycopy(serialized, 0, result, pos, serialized.length);
            Util.writeShort((short) length, result, result.length - OFFSET_SPACE);
            return result;
        }
    }

    private int uncompressedLengthOffset(byte[] bytes) {
        return bytes.length - OFFSET_SPACE;
    }

    private int uncompressedLength(byte[] bytes) {
        return Util.readShort(bytes, uncompressedLengthOffset(bytes));
    }

    public ArrayList<T> unpack(byte[] bytes) throws IOException, ClassNotFoundException {
        ArrayList<T> list = new ArrayList<>();
        int rawLength = uncompressedLength(bytes);
        int uncompressedStart = bytes.length - rawLength - OFFSET_SPACE;
        int offset = 0;
        while (offset < uncompressedStart) { // read all compressed blocks
            int blockLength = Util.readShort(bytes, offset);
            read(new GZIPInputStream(new ByteArrayInputStream(bytes, offset + OFFSET_SPACE, blockLength)), list);
            offset += blockLength + OFFSET_SPACE;
        }
        read(new ByteArrayInputStream(bytes, uncompressedStart, rawLength), list);
        return list;
    }
}
