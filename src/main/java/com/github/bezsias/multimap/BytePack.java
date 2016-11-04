package com.github.bezsias.multimap;

class BytePack {
    private static byte[] EMPTY_BYTE_ARRAY = new byte[0];
    byte[] noncompressed;
    byte[] compressed;

    BytePack() {
        this.noncompressed = EMPTY_BYTE_ARRAY;
        this.compressed = null;
    }

    BytePack(byte[] bytes) {
        this.noncompressed = bytes;
        this.compressed = null;
    }

    boolean isCompressed() {
        return compressed != null;
    }

    void appendCompressed(byte[] bytes) {
        if (compressed == null) {
            compressed = bytes;
        } else {
            byte[] result = new byte[compressed.length + bytes.length];
            System.arraycopy(compressed, 0, result, 0, compressed.length);
            System.arraycopy(bytes, 0, result, compressed.length, bytes.length);
            compressed = result;
        }
        noncompressed = EMPTY_BYTE_ARRAY;
    }

    int size() {
        if (compressed == null )
            return noncompressed.length;
        else
            return noncompressed.length + compressed.length;
    }
}
