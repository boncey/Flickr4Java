/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Common IO utilities.
 * 
 * @author Anthony Eden
 */
public class IOUtilities {

    private IOUtilities() {

    }

    public static void close(InputStream s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {

            }
        }
    }

    public static void close(OutputStream s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {

            }
        }
    }

    public static void close(Reader s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {

            }
        }
    }

    public static void close(Writer s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {

            }
        }
    }

}
