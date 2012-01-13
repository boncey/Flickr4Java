import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.test.Flickr;
import com.flickr4java.flickr.test.FlickrException;
import com.flickr4java.flickr.test.REST;
import com.flickr4java.flickr.test.RequestContext;
import com.flickr4java.flickr.test.auth.Auth;
import com.flickr4java.flickr.test.auth.AuthInterface;
import com.flickr4java.flickr.test.auth.Permission;
import com.flickr4java.flickr.test.util.IOUtilities;

/**
 * Demonstrates the authentication-process.<p>
 * 
 * If you registered API keys, you find them with the shared secret at your
 * <a href="http://www.flickr.com/services/api/registered_keys.gne">list of API keys</a>
 * 
 * @author mago
 * @version $Id: AuthExample.java,v 1.6 2009/08/25 19:37:45 x-mago Exp $
 */
public class AuthExample {
    Flickr f;
    RequestContext requestContext;
    String frob = "";
    String token = "";
    Properties properties = null;

    public AuthExample() throws ParserConfigurationException, IOException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);
        } finally {
            IOUtilities.close(in);
        }
        f = new Flickr(
            properties.getProperty("apiKey"),
            properties.getProperty("secret"),
            new REST("www.flickr.com")
        );
        Flickr.debugStream = false;
        requestContext = RequestContext.getRequestContext();
        AuthInterface authInterface = f.getAuthInterface();
        try {
            frob = authInterface.getFrob();
        } catch (FlickrException e) {
            e.printStackTrace();
        }
        System.out.println("frob: " + frob);
        URL url = authInterface.buildAuthenticationUrl(Permission.DELETE, frob);
        System.out.println("Press return after you granted access at this URL:");
        System.out.println(url.toExternalForm());
        BufferedReader infile =
          new BufferedReader ( new InputStreamReader (System.in) );
        String line = infile.readLine();
        try {
            Auth auth = authInterface.getToken(frob);
            System.out.println("Authentication success");
            // This token can be used until the user revokes it.
            System.out.println("Token: " + auth.getToken());
            System.out.println("nsid: " + auth.getUser().getId());
            System.out.println("Realname: " + auth.getUser().getRealName());
            System.out.println("Username: " + auth.getUser().getUsername());
            System.out.println("Permission: " + auth.getPermission().getType());
        } catch (FlickrException e) {
            System.out.println("Authentication failed");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            AuthExample t = new AuthExample();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
