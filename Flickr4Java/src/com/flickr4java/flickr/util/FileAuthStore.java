/**
 * 
 */
package com.flickr4java.flickr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.people.User;

/**
 * 
 * Implements a filesystem based storage system for Auth instances. One ".auth" file is maintained per Auth instance stored.
 * 
 * @author Matthew MacKenzie
 * 
 */
public class FileAuthStore implements AuthStore {

    private Map<String, Auth> auths;

    private File authStoreDir;

    public FileAuthStore(File authStoreDir) throws FlickrException {
        this.auths = new HashMap<String, Auth>();
        this.authStoreDir = authStoreDir;

        if (!authStoreDir.exists())
            authStoreDir.mkdir();

        if (!authStoreDir.canRead()) {
            try {
                throw new FlickrException("Cannot read " + authStoreDir.getCanonicalPath());
            } catch (IOException e) {
                throw new FlickrException(e.getMessage(), e);
            }
        }

        this.load();
    }

    private void load() throws FlickrException {
        try {
            File[] authFiles = authStoreDir.listFiles(new AuthFilenameFilter());

            for (int i = 0; i < authFiles.length; i++) {
                if (authFiles[i].isFile() && authFiles[i].canRead()) {
                    ObjectInputStream authStream = new ObjectInputStream(new FileInputStream(authFiles[i]));
                    Auth authInst = null;
                    try {
                        authInst = (Auth) authStream.readObject();
                    } catch (ClassCastException cce) {
                        // ignore. Its not an auth, so we won't store it. simple as that :-);
                    } catch (ClassNotFoundException e) {
                        // yep, ignoring. LALALALALLALAL. I can't hear you :-)
                    }
                    if (authInst != null) {
                        this.auths.put(authInst.getUser().getId(), authInst);
                    }
                }
            }
        } catch (IOException e) {
            throw new FlickrException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.flickr4java.flickr.util.AuthStore#store(com.flickr4java.flickr.auth.Auth)
     */
    public void store(Auth token) throws IOException {
        this.auths.put(token.getUser().getId(), token);

        String filename = token.getUser().getId() + ".auth";
        File outFile = new File(this.authStoreDir, filename);
        outFile.createNewFile();

        ObjectOutputStream authStream = new ObjectOutputStream(new FileOutputStream(outFile));
        authStream.writeObject(token);
        authStream.flush();
        authStream.close();

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.flickr4java.flickr.util.AuthStore#retrieve(java.lang.String)
     */
    public Auth retrieve(String nsid) {
        return this.auths.get(nsid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.flickr4java.flickr.util.AuthStore#retrieveAll()
     */
    public Auth[] retrieveAll() {
        return this.auths.values().toArray(new Auth[this.auths.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.flickr4java.flickr.util.AuthStore#clearAll()
     */
    public void clearAll() {
        this.auths.clear();
        File[] auths = this.authStoreDir.listFiles(new AuthFilenameFilter());
        for (int i = 0; i < auths.length; i++) {
            auths[i].delete();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.flickr4java.flickr.util.AuthStore#clear(java.lang.String)
     */
    public void clear(String nsid) {
        this.auths.remove(nsid);
        File auth = new File(this.authStoreDir, nsid + ".auth");
        if (auth.exists())
            auth.delete();

    }

    static class AuthFilenameFilter implements FilenameFilter {
        private static final String suffix = ".auth";

        public boolean accept(File dir, String name) {
            if (name.endsWith(suffix))
                return true;
            return false;
        }

    }

    public static void main(String[] args) throws Exception {
        FileAuthStore fas = new FileAuthStore(new File(System.getProperty("user.home") + File.separatorChar + "flickrauth"));
        Auth a = new Auth();
        User u = new User();
        u.setId("THISISMYNSID");
        a.setUser(u);
        fas.store(a);
        fas = null;

        fas = new FileAuthStore(new File(System.getProperty("user.home") + File.separatorChar + "flickrauth"));
        Auth a2 = fas.retrieve("THISISMYNSID");

        System.out.println(a2.getUser().getId());
    }
}
