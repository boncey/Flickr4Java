/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A FilterInputStream which will print all read data to the specified PrintWriter.
 * 
 * @author Anthony Eden
 */
public class DebugInputStream extends FilterInputStream {

    private OutputStream debugOut;

    /**
     * Creates a <code>FilterInputStream</code> by assigning the argument <code>in</code> to the field <code>this.in</code> so as to remember it for later use.
     * 
     * @param in
     *            the underlying input stream, or <code>null</code> if this instance is to be created without an underlying stream.
     */
    public DebugInputStream(InputStream in, OutputStream debugOut) {
        super(in);
        this.debugOut = debugOut;
    }

    public int read() throws IOException {
        int c = super.read();
        debugOut.write((char) c);
        return c;
    }

    public int read(byte[] b) throws IOException {
        int readCount = super.read(b);
        for (int i = 0; i < readCount; i++) {
            debugOut.write((char) b[i]);
        }
        return readCount;
    }

    public int read(byte[] b, int offset, int length) throws IOException {
        int readCount = super.read(b, offset, length);
        int readTo = offset + readCount;
        for (int i = offset; i < readTo; i++) {
            debugOut.write((char) b[i]);
        }
        return readCount;
    }

}
