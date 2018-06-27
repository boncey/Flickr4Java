import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.util.IOUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Demonstration of how to search.
 */
public class SearchExample
{
    static String apiKey;

    static String sharedSecret;

    Flickr f;

    REST rest;

    RequestContext requestContext;

    Properties properties;

    public SearchExample() throws IOException
    {
        String setupPropertiesPath = System.getenv("SETUP_PROPERTIES_PATH");

        InputStream in = null;
        try
        {
            if (setupPropertiesPath != null)
            {
                in = new FileInputStream(new File(setupPropertiesPath));
            } else
            {
                in = getClass().getResourceAsStream("/setup.properties");
            }
            properties = new Properties();
            properties.load(in);
        } finally
        {
            IOUtilities.close(in);
        }

        f = new Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), new REST());
        requestContext = RequestContext.getRequestContext();
        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(properties.getProperty("token"));
        auth.setTokenSecret(properties.getProperty("tokensecret"));
        requestContext.setAuth(auth);
        Flickr.debugRequest = false;
        Flickr.debugStream = false;
    }

    private void search(String text) throws FlickrException
    {
        PhotosInterface photos = f.getPhotosInterface();
        SearchParameters params = new SearchParameters();
        params.setMedia("videos"); // One of "photos", "videos" or "all"
        params.setExtras(Stream.of("media").collect(Collectors.toSet()));
        params.setText(text);
        PhotoList<Photo> results = photos.search(params, 5, 0);

        results.forEach(p ->
        {
            System.out.println(String.format("Title: %s", p.getTitle()));
            System.out.println(String.format("Media: %s", p.getMedia()));
            System.out.println(String.format("Original Video URL: %s", p.getVideoOriginalUrl()));
        });

    }

    public static void main(String[] args) throws Exception
    {
        SearchExample t = new SearchExample();
        t.search(args.length == 0 ? "London" : args[0]);
    }

}
