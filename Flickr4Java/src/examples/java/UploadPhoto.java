import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
// import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
// import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.people.User;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Set;

import com.flickr4java.flickr.photosets.Photosets;



// import java.io.BufferedInputStream;
import java.io.File;
import java.io.FilenameFilter;
// import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
// import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
// import java.util.Map;
import java.util.Scanner;
// import java.io.ByteArrayOutputStream;
//import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStream;


// import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
// import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.prefs.PrefsInterface;

// import com.flickr4java.flickr.tags.Tag;

/**
 * A simple program to upload photos to a set. It checks for files already uploaded assuming the title
 * is not changed so that it can be rerun if partial upload is done.
 *  It uses the tag field to store the filename as OrigFileName to be used while downloading
 * if the title has been changed.
 * If setup.properties is not available, pass the apiKey and secret as arguments to the program.
 * 
 * This sample also uses the AuthStore interface, so users will only be asked to authorize on the first run.
 * 
 * @author Keyur Parikh
 */

public class UploadPhoto {

    private static final Logger logger = Logger.getLogger(UploadPhoto.class);

    private  String nsid;
	private String username;

    // private final String sharedSecret;

    private final Flickr flickr;

    private AuthStore authStore;
	public boolean flickr_debug = false;
	private boolean setorigfilenametag = true;
    private boolean replace_spaces = false;

	private int privacy = -1;

    HashMap<String, Photoset> all_sets_map = new HashMap<String, Photoset>();
    HashMap<String, ArrayList<String>> set_name_to_id = new HashMap<String, ArrayList<String>>();
    public static final SimpleDateFormat smp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss a");

    public UploadPhoto(String apiKey, String nsid, String sharedSecret, File authsDir, String username) throws FlickrException {
        flickr = new Flickr(apiKey, sharedSecret, new REST());
       
        this.username = username;
        this.nsid = nsid;
        // this.sharedSecret = sharedSecret;

        if (authsDir != null) {
            this.authStore = new FileAuthStore(authsDir);
        }
        
        // If one of them is not filled in, find and populate it.
        if(username == null || username.equals(""))
        	set_username();
        if(nsid == null || nsid.equals(""))
            	set_nsid(); 

     }

    private void set_username() throws FlickrException {
    	if(nsid != null && !nsid.equals("")) {
    		Auth auth = null;
    		if (authStore != null) {
    			auth = authStore.retrieve(nsid);
    			if (auth != null) {
    				username = auth.getUser().getUsername();
    			}
    		}
    		// For this to work: REST.java or PeopleInterface needs to change to pass api_key
    		//		as the parameter to the call which is not authenticated.

    		if(auth == null) {
    			// Get nsid using flickr.people.findByUsername
    			PeopleInterface people_i = flickr.getPeopleInterface();
    			User u = people_i.getInfo(nsid);
    			if(u != null) {
    				username = u.getUsername();
    			}
    		}
    	}
	}
    /**
	* Check local saved copy first ??.
	* If Auth by username is available, then we will not need to make the API call.
     * 
     * @throws FlickrException
     */

	    private void set_nsid() throws FlickrException {
    
		if(username != null && !username.equals("")) {
			Auth auth = null;
			if (authStore != null) {
				auth = authStore.retrieve(username);  // assuming FileAuthStore is enhanced else need to
							// keep in user-level files.
				
				if (auth != null) {
					nsid = auth.getUser().getId();
				}
			}
			if(auth != null)
				return;

			Auth[] all_auths = authStore.retrieveAll();
			for(int i = 0 ; i < all_auths.length; i++) {
				if(username.equals(all_auths[i].getUser().getUsername())) {
					nsid = all_auths[i].getUser().getId();
					return;
				}
			}
			
			// For this to work: REST.java or PeopleInterface needs to change to pass api_key
			//		as the parameter to the call which is not authenticated.

			// Get nsid using flickr.people.findByUsername
			PeopleInterface people_i = flickr.getPeopleInterface();
			User u = people_i.findByUsername(username);
			if(u != null) {
				nsid = u.getId();
			}
		}
	}
	    
    private void authorize() throws IOException, SAXException, FlickrException {
        AuthInterface authInterface = flickr.getAuthInterface();
        Token accessToken = authInterface.getRequestToken();

        // Try with DELETE permission. At least need write permission for upload and add-to-set.
        String url = authInterface.getAuthorizationUrl(accessToken, Permission.DELETE);
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

        Scanner scanner = new Scanner(System.in);
        String tokenKey = scanner.nextLine();

        Token requestToken = authInterface.getAccessToken(accessToken, new Verifier(tokenKey));

        Auth auth = authInterface.checkToken(requestToken);
        RequestContext.getRequestContext().setAuth(auth);
        this.authStore.store(auth);
        scanner.close();
        System.out.println("Thanks.  You probably will not have to do this every time. Auth saved for user: " 
    			+ auth.getUser().getUsername() + " nsid is: " +  auth.getUser().getId());
        System.out.println(" AuthToken: " + auth.getToken() + " tokenSecret: " + auth.getTokenSecret());
    }
/**
 * If the Authtoken was already created in a separate program but not saved to file.
 * 
 * @param authToken
 * @param tokenSecret
 * @param username
 * @return
 * @throws IOException
 */
    private Auth constructAuth(String authToken, String tokenSecret, String username) throws IOException {
    	
        Auth auth = new Auth();
        auth.setToken(authToken);
        auth.setTokenSecret(tokenSecret);
        
        // Prompt to ask what permission is needed: read, update or delete.
        auth.setPermission(Permission.fromString("delete") );

        User user = new User();
        // Later change the following 3. Either ask user to pass on command line or read
        //		from saved file.
        user.setId(nsid);
        user.setUsername((username));
        user.setRealName("");
        auth.setUser(user);
        this.authStore.store(auth);
        return auth;
    }

    public void setAuth(String authToken, String username, String token_secret) throws IOException, SAXException, FlickrException {
        RequestContext rc = RequestContext.getRequestContext();
        Auth auth = null;
     
        if(authToken != null && !authToken.equals("") && token_secret != null && !token_secret.equals("")) {
        	auth = constructAuth(authToken, token_secret, username);
        	rc.setAuth(auth);
    	}
        else {
        	if (this.authStore != null) {
        		auth = this.authStore.retrieve(this.nsid);
        		if (auth == null) {
        			this.authorize();
        		} else {
        			rc.setAuth(auth);
        		}
        	}
        }
    }

    public int getPrivacy() throws Exception {

    	PrefsInterface prefi = flickr.getPrefsInterface();
    	privacy = prefi.getPrivacy();

    	return(privacy);
    }

	private String makeSafeFilename(String input) {
        byte[] fname = input.getBytes();
        byte[] bad = new byte[] { '\\', '/', '"', '*' };
        byte replace = '_';
        for (int i = 0; i < fname.length; i++) {
            for (byte element : bad) {
                if (fname[i] == element) {
                    fname[i] = replace;
                }
            }
            if(replace_spaces && fname[i] == ' ')
            	fname[i] = '_';
        }
        return new String(fname);
    }

    public String uploadfile(String filename, String inp_title) throws Exception {
    	String photoId;

    	RequestContext rc = RequestContext.getRequestContext();

        if (this.authStore != null) {
            Auth auth = this.authStore.retrieve(this.nsid);
            if (auth == null) {
                this.authorize();
            } else {
                rc.setAuth(auth);
            }
        }

        //PhotosetsInterface pi = flickr.getPhotosetsInterface();
        // PhotosInterface photoInt = flickr.getPhotosInterface();
        // Map<String, Collection> allPhotos = new HashMap<String, Collection>();
/**

    1 : Public
    2 : Friends only
    3 : Family only
    4 : Friends and Family
    5 : Private

**/
	if( privacy == -1)
		getPrivacy();

	UploadMetaData metaData = new UploadMetaData();

	if(privacy == 1)
		metaData.setPublicFlag(true);
	if(privacy == 2 || privacy == 4)
		metaData.setFriendFlag(true);
	if(privacy == 3 || privacy == 4)
		metaData.setFamilyFlag(true);
		
	if(basefilename == null || basefilename.equals(""))
		basefilename = filename; // "image.jpg";
	
	String title = basefilename;
	boolean setmimetype = true;	// change during testing. Doesn't seem to be supported at this time in flickr.
	if(setmimetype) {
		if(basefilename.lastIndexOf('.') > 0) {
			title = basefilename.substring(0, basefilename.lastIndexOf('.') );
			String suffix = basefilename.substring(basefilename.lastIndexOf('.')+1);
			// Set Mime Type if known.

			// Later use a mime-type properties file or a hash table of all known photo and video types
			//	allowed by flickr.
			
			if(suffix.equalsIgnoreCase("png")) {
				metaData.setFilemimetype("image/png");
			}
			else if(suffix.equalsIgnoreCase("mpg") ||suffix.equalsIgnoreCase("mpeg") ) {
				metaData.setFilemimetype("video/mpeg");
			}
			else if(suffix.equalsIgnoreCase("mov")) {
				metaData.setFilemimetype("video/quicktime");
			}
		}
	}
	logger.debug(" File : " + filename );
	logger.debug(" basefilename : " + basefilename );
	
	if(inp_title != null && !inp_title.equals("")) {
		title = inp_title;
		logger.debug(" title : " + inp_title );	
        metaData.setTitle(title);	
	} // flickr defaults the title field from file name.

	// UploadMeta is using String not Tag class.

	// Tags are getting mangled by yahoo stripping off the = ,  '.' and many other punctuation characters
	//	and converting to lower case: use the raw tag field to find the real value for checking and
	//	for download.
	if(setorigfilenametag) {
		List<String> tags = new ArrayList<String>();
		String tmp = basefilename;
		basefilename = makeSafeFilename(basefilename);
		tags.add("OrigFileName='" + basefilename + "'");
		metaData.setTags(tags);
		
		if(!tmp.equals(basefilename)){
			System.out.println(" File : " + basefilename + " contains special characters.  stored as " + basefilename + " in tag field" );
		}
	}

        // File imageFile = new File(filename);
        // InputStream in = null;
        Uploader uploader = flickr.getUploader();

       //  ByteArrayOutputStream out = null;
        try {
            // in = new FileInputStream(imageFile);
            // out = new ByteArrayOutputStream();
        	
            // int b = -1;
	    /**
            while ((b = in.read()) != -1) {
                out.write((byte) b);
            }
	    **/

	    /**
	    byte[] buf = new byte[1024];
	    while ((b = in.read(buf)) != -1) {
			// fos.write(read);
			out.write(buf, 0, b);
		}
		**/

            metaData.setFilename(basefilename);
            // check correct handling of escaped value

            File f = new File(filename);
            photoId = uploader.upload(f, metaData);

            logger.debug(" File : " + filename + " uploaded: photoId = " + photoId);
        } finally {
        	
        }

        return(photoId);
    }

    public void get_photosets_info() {
    	
        PhotosetsInterface pi = flickr.getPhotosetsInterface();
        try {
        	int sets_page = 1;
        	while (true) {
        		Photosets photosets = pi.getList(nsid, 500, sets_page);
        		Collection<Photoset> sets_coll = photosets.getPhotosets();
        		Iterator<Photoset> sets_iter = sets_coll.iterator();
        		while (sets_iter.hasNext()) {
        			Photoset set = sets_iter.next();
        			all_sets_map.put(set.getId(), set);

        			// 2 or more sets can in theory have the same name. !!!
        			ArrayList<String> set_idarr = set_name_to_id.get(set.getTitle());
        			if(set_idarr == null) {
        				set_idarr = new ArrayList<String>();
        				set_idarr.add(new String(set.getId()));
        				set_name_to_id.put(set.getTitle(), set_idarr);
        			}
        			else {
        				set_idarr.add(new String(set.getId()));
        			}
        		}

        		if (sets_coll.size() < 500) {
        			break;
        		}
        		sets_page++;
        	}
        	logger.debug(" Sets retrieved: " + all_sets_map.size());
        	// all_sets_retrieved = true;
        	// Print dups if any.

        	Set<String> keys = set_name_to_id.keySet();
        	Iterator<String> iter = keys.iterator();
        	while(iter.hasNext()) {
        		String name = iter.next();
        		ArrayList<String> set_idarr = set_name_to_id.get(name);
        		if(set_idarr != null && set_idarr.size() > 1) {
        			System.out.println("There is more than 1 set with this name : " + set_name_to_id.get(name));
        			for(int j = 0; j < set_idarr.size(); j++) {
        				System.out.println("           id: " + set_idarr.get(j));
        			}
        		}
        	}

        } catch (FlickrException e) {
        	e.printStackTrace();
        }
    }

    private String setid = null;
    private String basefilename = null;
	private PhotoList<Photo> photos = new PhotoList<Photo>();
    private HashMap<String, Photo> file_photos = new HashMap<String, Photo>();

	private static void Usage() {
        System.out.println("Usage: java " + UploadPhoto.class.getName() + "  [ -n nsid | -u username ] -s setName { File../Directories}");
        System.out.println("	Must pass either -u username or -n nsid ");
        System.out.println("	Must pass  -s followed by set-name(albums)  followed by file/directories.");
        System.out.println("apiKey and shared secret must be available as apiKey and secret via setup.properties or passed as -apiKey key -secret shared-secret");
        System.exit(1);		
	}

    /**
	 * @return the setorigfilenametag
	 */
	public boolean isSetorigfilenametag() {
		return setorigfilenametag;
	}

	/**
	 * @param setorigfilenametag the setorigfilenametag to set
	 */
	public void setSetorigfilenametag(boolean setorigfilenametag) {
		this.setorigfilenametag = setorigfilenametag;
	}

	public static void main(String[] args) throws Exception {
    	
        String api_key = null; // args[0];
        String shared_secret = null; // args[1];

        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = UploadPhoto.class.getResourceAsStream("/setup.properties");
            if(in != null) {
                    properties.load(in);
                	api_key = properties.getProperty("apiKey");
                    shared_secret = properties.getProperty("secret");
                    if(api_key != null && shared_secret != null)
                    	logger.debug("Found setup.properties in classpath and set apiKey and shared secret");
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
         finally {
            if(in != null)
                    in.close();
        }

        if (args.length < 5) {
    			Usage();
    			System.exit(1);
    	}
    	
    	ArrayList<String> uploadfile_args = new ArrayList<String>();
    	ArrayList<String> option_args = new ArrayList<String>();

    	// Flickr.debugRequest = true;		// keep it false else entire file will be in stdout.

    	// Flickr.debugStream = true;

    	String authsDir_str = System.getProperty("user.home") + File.separatorChar + ".flickrAuth";

        String nsid = null;
        String user_name = null;
        String access_token = null; // Optional entry.
        String token_secret = null;	// Optional entry.
    	String setName = null; 
    	
    	boolean settagname = true;	// Default to true to add tag while uploading.
    	

        int i = 0;
        /***
        for(i = 0; i < args.length; i++) {
        	System.out.println("args[" + i + "] " + args[i]);
        }
        **/
        
        for(i = 0; i < args.length; i++) {
        	switch(args[i]) {
        	case "-n":        		if(i < args.length)
        								nsid = args[++i];
        							break;
        	case "-u":				if(i < args.length)
        								user_name = args[++i];
        							break;
        	case "-apiKey":			if(i < args.length)
        								api_key = args[++i];
									break;
									
        	case "-secret":			if(i < args.length)
        								shared_secret = args[++i];
									break;
        	case "-notags" :		if(i < args.length)
        								settagname = false;
        							break;
									
        	case "-a" :				if(i < args.length)
        								access_token = args[++i];
									break;
        	case "-t" :				if(i < args.length)
    									token_secret = args[++i];
        							break;
        	case "-s" :            	if(i < args.length)
										setName = args[++i];
									break;
        	case "-option":			
							if(i < args.length)
        								option_args.add(args[++i]);
        							break;
        	default : 			    if(setName != null)
        								uploadfile_args.add(args[i]);
        							else {
        								Usage();
        								System.exit(1);
        							}
        	}
        }
        
        if(api_key == null || shared_secret == null ||
        		(user_name == null && nsid == null) || (setName == null ) || 
        			(uploadfile_args.size() == 0) ) {
        	Usage();
			System.exit(1);        	
        }
    	
    	UploadPhoto bf = new UploadPhoto(api_key, nsid, shared_secret, new File(authsDir_str), user_name);
        for(i = 0; i < option_args.size(); i++) {
        	bf.add_option( option_args.get(i));
        }
    	bf.setSetorigfilenametag(settagname);
    	bf.setAuth(access_token, user_name, token_secret );

    	if(!bf.can_upload())
    		System.exit(1);

    	bf.getPrivacy();
    	
    	bf.get_photosets_info();

    	if(setName != null && !setName.equals("")) {
    		
    		bf.get_set_photos(setName);
    	}

    	// String photoid; 

    	for( i = 0; i < uploadfile_args.size(); i++) {
    		String filename = uploadfile_args.get(i);
    		
    		File f = new File(filename);
    		if(f.isDirectory()) {
    			String[] filelist = f.list(new UploadFilenameFilter());
    			logger.debug("Processing directory  : " + uploadfile_args.get(i) );
    			for(int j = 0; j < filelist.length; j++) {
        			bf.process_file_arg( uploadfile_args.get(i) + File.separatorChar + filelist[j],  setName);
    			}
    		}
    		else {
    			bf.process_file_arg( filename,  setName);
    		}
    	}
    }
    
    private static final String[] photo_suffixes = {
		"jpg"
		,"jpeg"
		,"png"
		,"gif"
		,"bmp"
		,"tif","tiff" };
	
    private static final String[] video_suffixes = {
    				"3gp","3gp","avi","mov","mp4","mpg","mpeg","wmv","ogg","ogv","m2v"};
    
    static class UploadFilenameFilter implements FilenameFilter {
    	
    	// Following suffixes from flickr upload page. An App should have this configurable,
    	// for videos and photos separately.

        public boolean accept(File dir, String name) {
        	if(is_valid_suffix(name))
        		return true;
        	else
        		return false;
        }

    }

    private static boolean is_valid_suffix(String basefilename) {
    	if(basefilename.lastIndexOf('.') <= 0) {
    		return false;
    	}
		String suffix = basefilename.substring(basefilename.lastIndexOf('.')+1).toLowerCase();
		for(int i = 0; i < photo_suffixes.length; i++) {
			if(photo_suffixes[i].equals(suffix))
				return true;
		}
		for(int i = 0; i < video_suffixes.length; i++) {
			if(video_suffixes[i].equals(suffix))
				return true;
		}
		logger.debug(basefilename + " does not have a valid suffix, skipped.");
        return false;    	
    }
    
    private void process_file_arg(String filename, String setName) throws Exception {
    	String photoid; 
		if(filename.equals(""))  {
			System.out.println("filename must be entered for uploadfile " );
			return;
		}
		if(filename.lastIndexOf(File.separatorChar ) > 0)
			basefilename = filename.substring(filename.lastIndexOf(File.separatorChar ) + 1, filename.length());
		else
			basefilename = filename;

		boolean file_uploaded = check_if_loaded(filename);

		if(! file_uploaded) {
        	if(!is_valid_suffix(basefilename)) {
        		System.out.println(" File: " + basefilename + " is not a supported filetype for flickr (invalid suffix)");
        		return;
        	}

        	File f = new File(filename);
        	if(!f.exists() || !f.canRead()) {
        		System.out.println(" File: " + filename + " cannot be processed, does not exist or is unreadable.");
        		return;        		
        	}
        	logger.debug("Calling uploadfile for filename : " + filename );
        	logger.info("Upload of " + filename + " started at: " + smp.format(new Date()) + "\n");

			photoid = uploadfile(filename, null);
			// Add to Set. Create set if it does not exist.
			if(photoid != null) {
				add_photo_to_set( photoid, setName);
			}
			logger.info("Upload of " + filename + " finished at: " + smp.format(new Date()) + "\n");
	        
		} else {
			logger.info(" File: " + filename + " has already been loaded on " + get_uploaded_time(filename) );
		}    	
    }

	private void add_option(String opt) {

		switch(opt) {
		case "replace_spaces":		replace_spaces = true;
							break;

		case "notags":		setSetorigfilenametag(false);
							break;
									
									
		default:			// Not supported at this time.
							System.out.println("Option: " + opt + " is not supported at this time");
		}
	}

    
    private boolean can_upload() {
    	RequestContext rc = RequestContext.getRequestContext();
    	Auth auth = null;
    	auth = rc.getAuth();
    	if(auth == null) {
    		System.out.println(" Cannot upload, there is no authorization information.");
    		return false;
    	}
    	Permission perm = auth.getPermission();
    	if( (perm.getType() == Permission.WRITE_TYPE) || (perm.getType() == Permission.DELETE_TYPE))
    		return true;
    	else {
    		System.out.println(" Cannot upload, You need write or delete permission, you have : " + perm.toString());    		
    		return false;
    	}
    }

/**
 * The assumption here is that for a given set only unique file-names will be loaded and the title
 * field can be used. Later change to use the tags field ( OrigFileName) and strip off the suffix.
 * 
 * @param filename
 * @return
 */
    private  boolean check_if_loaded(String filename) {

    	String title;
		if(basefilename.lastIndexOf('.') > 0)
			title = basefilename.substring(0, basefilename.lastIndexOf('.') );
		else
			return false;
		
		if(file_photos.containsKey(title))
			return true;
		
		return false;
	}

    private  String get_uploaded_time(String filename) {

    	String title = "";
		if(basefilename.lastIndexOf('.') > 0)
			title = basefilename.substring(0, basefilename.lastIndexOf('.') );
		
		if(file_photos.containsKey(title)) {
			Photo p = file_photos.get(title);
			if(p.getDatePosted() != null) {
				return(smp.format(p.getDatePosted()));
			}
		}
		
		return "";
	}

	private  void get_set_photos(String setName) throws FlickrException {
    	// Check if this is an existing set. If it is get all the photo list to avoid reloading already
    	// 	loaded photos.
	    ArrayList<String> set_idarr ;
	    set_idarr = set_name_to_id.get(setName);
    	if(set_idarr != null) {
    		setid = set_idarr.get(0);
    	    PhotosetsInterface pi = flickr.getPhotosetsInterface();

    	    Set<String> extras = new HashSet<String>();
    	    /**
    	     *     A comma-delimited list of extra information to fetch for each returned record.
    	     *      Currently supported fields are: license, date_upload, date_taken, owner_name,
    	     *       icon_server, original_format, last_update, geo, tags, machine_tags, o_dims,
    	     *        views, media, path_alias, url_sq, url_t, url_s, url_m, url_o
    	     */
    	    
    	    extras.add("date_upload");
    	    extras.add("original_format");
    	    extras.add("media");
    	    // extras.add("url_o");
    	    extras.add("tags");

    		int setPage = 1;
    		while (true) {
    		    PhotoList<Photo> tmp_set = pi.getPhotos(setid, extras, Flickr.PRIVACY_LEVEL_NO_FILTER, 500, setPage);
    		   
    		    int tmp_set_size = tmp_set.size();
    		    photos.addAll(tmp_set);
    		    if (tmp_set_size < 500) {
    		    	break;
    		    }
    		    setPage++;
    		}
    		for(int i = 0; i < photos.size(); i++) {
    			file_photos.put(photos.get(i).getTitle(), photos.get(i));
    		}
        	if(flickr_debug) {
        		logger.debug("Set title: " + setName + "  id:  " + setid + " found");
        		logger.debug("   Photos in Set already loaded: " +  photos.size() );
        	}
    	}
	}

	public void add_photo_to_set(String photoid, String setName) throws Exception {

	    ArrayList<String> set_idarr ;

	    // all_set_maps.

		PhotosetsInterface psets_i = flickr.getPhotosetsInterface();

		Photoset set = null;
		
	    if(setid == null) {
	    	// In case it is a new photo-set.	
	    	set_idarr = set_name_to_id.get(setName);
	    	if(set_idarr == null) {
	    		// set_idarr should be null since we checked it get_set_photos.
	    		// Create the new set.
	    		// set the setid .

	    		String description = "";
	    		set = psets_i.create(setName, description, photoid);
	    		setid = set.getId();

	    		set_idarr = new ArrayList<String>();
	    		set_idarr.add(new String(setid));
	    		set_name_to_id.put(setName, set_idarr);

	    		all_sets_map.put(set.getId(), set);
	    	}
	    }
	    else {
	    	set = all_sets_map.get(setid);
		    psets_i.addPhoto(setid , photoid);
	    }
	    // Add to photos .
	    
	    // Add Photo to existing set.
	    PhotosInterface photoInt = flickr.getPhotosInterface();
	    Photo p = photoInt.getPhoto(photoid);
	    if(p != null) {
	    	photos.add(p);
	    	String title;
			if(basefilename.lastIndexOf('.') > 0)
				title = basefilename.substring(0, basefilename.lastIndexOf('.') );
			else
				title = p.getTitle();
			file_photos.put(title, p);
	    }
    }
}

