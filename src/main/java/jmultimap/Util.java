package jmultimap;

import java.io.*;

public class Util {
    private static int BUFFER_SIZE = 8192;
    public static byte ZERO_BYTE   = (byte) 0;

    public static void copy(InputStream from, OutputStream to)
            throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        int len;
        while((len = from.read(buf)) != -1)
            to.write(buf, 0, len);
    }

    public static void writeShort(short value, byte[] array, int pos) {
        array[pos] = (byte)((value >> 8) & 0xFF);
        array[pos+1] = (byte)(value & 0xFF);
    }

    public static short readShort(byte[] array, int pos) {
        return (short)((array[pos] & 0xFF) << 8 | (array[pos+1] & 0xFF));
    }
}
