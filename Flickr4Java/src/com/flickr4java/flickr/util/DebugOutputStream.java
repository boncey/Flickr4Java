/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.flickr4java.flickr.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A FilterInputStream which will print all read data to the specified PrintWriter.
 * 
 * @author Anthony Eden
 */
public class DebugOutputStream extends FilterOutputStream {

    private OutputStream debugOut;

    /**
     * Creates a <code>FilterInputStream</code> by assigning the argument <code>in</code> to the field <code>this.in</code> so as to remember it for later use.
     * 
     * @param out
     *            the underlying output stream, or <code>null</code> if this instance is to be created without an underlying stream.
     */
    public DebugOutputStream(OutputStream out, OutputStream debugOut) {
        super(out);
        this.debugOut = debugOut;
    }

    public void write(int b) throws IOException {
        super.write(b);
        debugOut.write((char) b);
    }

    public void write(byte[] b) throws IOException {
        super.write(b);
        debugOut.write(b);
    }

    public void write(byte[] b, int offset, int length) throws IOException {
        super.write(b, offset, length);
        debugOut.write(b, offset, length);
    }

}
