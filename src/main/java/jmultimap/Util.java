package jmultimap;

import java.io.*;

class Util {
    static int BUFFER_SIZE = 8192;
    static byte ZERO_BYTE   = (byte) 0;

    static void copy(InputStream from, OutputStream to)
            throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        int len;
        while((len = from.read(buf)) != -1)
            to.write(buf, 0, len);
    }

    static void writeShort(short value, byte[] array, int pos) {
        array[pos] = (byte)((value >> 8) & 0xFF);
        array[pos+1] = (byte)(value & 0xFF);
    }

    static short readShort(byte[] array, int pos) {
        return (short)((array[pos] & 0xFF) << 8 | (array[pos+1] & 0xFF));
    }
}
