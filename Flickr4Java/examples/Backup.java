import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * A simple program to backup all of a users private and public photos in a photoset aware manner. If photos are classified in multiple photosets, they will be
 * copied. Its a sample, its not perfect :-)
 * 
 * This sample also uses the AuthStore interface, so users will only be asked to authorize on the first run.
 * 
 * @author Matthew MacKenzie
 * @version $Id: Backup.java,v 1.6 2009/01/01 16:44:57 x-mago Exp $
 */

public class Backup {

    private final String nsid;

    private final Flickr flickr;

    private AuthStore authStore;

    public Backup(String apiKey, String nsid, String sharedSecret, File authsDir) throws FlickrException {
        flickr = new Flickr(apiKey, sharedSecret, new REST());
        this.nsid = nsid;

        if (authsDir != null) {
            this.authStore = new FileAuthStore(authsDir);
        }
    }

    private void authorize() throws IOException, SAXException, FlickrException {
        AuthInterface authInterface = flickr.getAuthInterface();
        Token accessToken = authInterface.getRequestToken();

        String url = authInterface.getAuthorizationUrl(accessToken, Permission.READ);
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

        String tokenKey = new Scanner(System.in).nextLine();

        Token requestToken = authInterface.getAccessToken(accessToken, new Verifier(tokenKey));

        Auth auth = authInterface.checkToken(requestToken);
        RequestContext.getRequestContext().setAuth(auth);
        this.authStore.store(auth);
        System.out.println("Thanks.  You probably will not have to do this every time.  Now starting backup.");
    }

    public void doBackup(File directory) throws Exception {
        if (!directory.exists()) {
            directory.mkdir();
        }

        RequestContext rc = RequestContext.getRequestContext();

        if (this.authStore != null) {
            Auth auth = this.authStore.retrieve(this.nsid);
            if (auth == null) {
                this.authorize();
            } else {
                rc.setAuth(auth);
            }
        }

        PhotosetsInterface pi = flickr.getPhotosetsInterface();
        PhotosInterface photoInt = flickr.getPhotosInterface();
        Map<String, Collection> allPhotos = new HashMap<String, Collection>();

        Iterator sets = pi.getList(this.nsid).getPhotosets().iterator();

        while (sets.hasNext()) {
            Photoset set = (Photoset) sets.next();
            PhotoList photos = pi.getPhotos(set.getId(), 500, 1);
            allPhotos.put(set.getTitle(), photos);
        }

        int notInSetPage = 1;
        Collection notInASet = new ArrayList();
        while (true) {
            Collection nis = photoInt.getNotInSet(50, notInSetPage);
            notInASet.addAll(nis);
            if (nis.size() < 50) {
                break;
            }
            notInSetPage++;
        }
        allPhotos.put("NotInASet", notInASet);

        Iterator allIter = allPhotos.keySet().iterator();

        while (allIter.hasNext()) {
            String setTitle = (String) allIter.next();
            String setDirectoryName = makeSafeFilename(setTitle);

            Collection currentSet = allPhotos.get(setTitle);
            Iterator setIterator = currentSet.iterator();
            File setDirectory = new File(directory, setDirectoryName);
            setDirectory.mkdir();
            while (setIterator.hasNext()) {

                Photo p = (Photo) setIterator.next();
                String url = p.getLargeUrl();
                URL u = new URL(url);
                String filename = u.getFile();
                filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
                System.out.println("Now writing " + filename + " to " + setDirectory.getCanonicalPath());
                BufferedInputStream inStream = new BufferedInputStream(photoInt.getImageAsStream(p, Size.LARGE));
                File newFile = new File(setDirectory, filename);

                FileOutputStream fos = new FileOutputStream(newFile);

                int read;

                while ((read = inStream.read()) != -1) {
                    fos.write(read);
                }
                fos.flush();
                fos.close();
                inStream.close();
            }
        }

    }

    private String makeSafeFilename(String input) {
        byte[] fname = input.getBytes();
        byte[] bad = new byte[] { '\\', '/', '"' };
        byte replace = '_';
        for (int i = 0; i < fname.length; i++) {
            for (byte element : bad) {
                if (fname[i] == element) {
                    fname[i] = replace;
                }
            }
        }
        return new String(fname);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Usage: java " + Backup.class.getName() + " api_key nsid shared_secret output_dir");
            System.exit(1);
        }
        Backup bf = new Backup(args[0], args[1], args[2], new File(System.getProperty("user.home") + File.separatorChar + ".flickrAuth"));
        bf.doBackup(new File(args[3]));
    }
}
