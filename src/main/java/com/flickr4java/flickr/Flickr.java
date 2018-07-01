/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.flickr4java.flickr;

import com.flickr4java.flickr.activity.ActivityInterface;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.blogs.BlogsInterface;
import com.flickr4java.flickr.cameras.CamerasInterface;
import com.flickr4java.flickr.collections.CollectionsInterface;
import com.flickr4java.flickr.commons.CommonsInterface;
import com.flickr4java.flickr.contacts.ContactsInterface;
import com.flickr4java.flickr.favorites.FavoritesInterface;
import com.flickr4java.flickr.galleries.GalleriesInterface;
import com.flickr4java.flickr.groups.GroupsInterface;
import com.flickr4java.flickr.groups.discuss.GroupDiscussInterface;
import com.flickr4java.flickr.groups.members.MembersInterface;
import com.flickr4java.flickr.groups.pools.PoolsInterface;
import com.flickr4java.flickr.interestingness.InterestingnessInterface;
import com.flickr4java.flickr.machinetags.MachinetagsInterface;
import com.flickr4java.flickr.panda.PandaInterface;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.comments.CommentsInterface;
import com.flickr4java.flickr.photos.geo.GeoInterface;
import com.flickr4java.flickr.photos.licenses.LicensesInterface;
import com.flickr4java.flickr.photos.notes.NotesInterface;
import com.flickr4java.flickr.photos.suggestions.SuggestionsInterface;
import com.flickr4java.flickr.photos.transform.TransformInterface;
import com.flickr4java.flickr.photos.upload.UploadInterface;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.photosets.comments.PhotosetsCommentsInterface;
import com.flickr4java.flickr.places.PlacesInterface;
import com.flickr4java.flickr.prefs.PrefsInterface;
import com.flickr4java.flickr.reflection.ReflectionInterface;
import com.flickr4java.flickr.stats.StatsInterface;
import com.flickr4java.flickr.tags.TagsInterface;
import com.flickr4java.flickr.test.TestInterface;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.uploader.UploaderResponse;
import com.flickr4java.flickr.urls.UrlsInterface;

import java.util.Set;

/**
 * Main entry point for the Flickr4Java API. This class is used to acquire Interface classes which wrap the Flickr API.
 * <p>
 * 
 * If you registered API keys, you find them with the shared secret at your <a href="http://www.flickr.com/services/api/registered_keys.gne">list of API
 * keys</a>
 * <p>
 * 
 * The user who authenticates himself, can manage this permissions at <a href="http://www.flickr.com/services/auth/list.gne">his list of Third-party
 * applications</a> (You -> Your account -> Extending Flickr -> Account Links -> edit).
 * 
 * @author Anthony Eden
 * @version $Id: Flickr.java,v 1.45 2009/06/23 21:51:25 x-mago Exp $
 */
public class Flickr implements IFlickr {

    /**
     * The default endpoint host.
     */
    public static final String DEFAULT_HOST = "api.flickr.com";

    /**
     * The key used when the API key is stored for passing to the Transport methods.
     */
    public static final String API_KEY = "api_key";

    /**
     * Set to true to enable response debugging (print the response stream)
     */
    public static boolean debugStream = false;

    /**
     * Set to true to enable request debugging (print the request stream, used for "post")
     */
    public static boolean debugRequest = false;

    private String apiKey;

    private String sharedSecret;

    private Transport transport;

    private Auth auth;

    private AuthInterface authInterface;

    private ActivityInterface activityInterface;

    private BlogsInterface blogsInterface;

    private CommentsInterface commentsInterface;

    private CommonsInterface commonsInterface;

    private ContactsInterface contactsInterface;

    private FavoritesInterface favoritesInterface;

    private GeoInterface geoInterface;

    private GroupsInterface groupsInterface;

    private InterestingnessInterface interestingnessInterface;

    private LicensesInterface licensesInterface;

    private MembersInterface membersInterface;

    private MachinetagsInterface machinetagsInterface;

    private NotesInterface notesInterface;

    private PandaInterface pandaInterface;

    private PoolsInterface poolsInterface;

    private PeopleInterface peopleInterface;

    private PhotosInterface photosInterface;

    private PhotosetsCommentsInterface photosetsCommentsInterface;

    private PhotosetsInterface photosetsInterface;

    private CollectionsInterface collectionsInterface;

    private PlacesInterface placesInterface;

    private PrefsInterface prefsInterface;

    private ReflectionInterface reflectionInterface;

    private TagsInterface tagsInterface;

    private TestInterface testInterface;

    private TransformInterface transformInterface;

    private UploadInterface uploadInterface;

    private Uploader uploader;

    private UrlsInterface urlsInterface;

    private GalleriesInterface galleriesInterface;

    private StatsInterface statsInterface;

    private CamerasInterface cameraInterface;

    private SuggestionsInterface suggestionsInterface;

    private GroupDiscussInterface discussionInterface;

    /**
     * @see com.flickr4java.flickr.photos.PhotosInterface#setContentType(String, String)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getContentType()
     * @see com.flickr4java.flickr.uploader.UploadMetaData#setContentType(String)
     */
    public static final String CONTENTTYPE_PHOTO = "1";

    /**
     * @see com.flickr4java.flickr.photos.PhotosInterface#setContentType(String, String)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getContentType()
     * @see com.flickr4java.flickr.uploader.UploadMetaData#setContentType(String)
     */
    public static final String CONTENTTYPE_SCREENSHOT = "2";

    /**
     * @see com.flickr4java.flickr.photos.PhotosInterface#setContentType(String, String)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getContentType()
     * @see com.flickr4java.flickr.uploader.UploadMetaData#setContentType(String)
     */
    public static final String CONTENTTYPE_OTHER = "3";

    /**
     * The lowest accuracy for bounding-box searches.
     * 
     * @see com.flickr4java.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_WORLD = 1;

    /**
     * @see com.flickr4java.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_COUNTRY = 3;

    /**
     * @see com.flickr4java.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_REGION = 6;

    /**
     * @see com.flickr4java.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_CITY = 11;

    /**
     * The highest accuracy for bounding-box searches.
     * 
     * @see com.flickr4java.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_STREET = 16;

    /**
     * @see com.flickr4java.flickr.photos.PhotosInterface#setSafetyLevel(String, String, Boolean)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getSafetyLevel()
     * @see com.flickr4java.flickr.uploader.UploadMetaData#setSafetyLevel(String)
     * @see com.flickr4java.flickr.photos.SearchParameters#setSafeSearch(String)
     */
    public static final String SAFETYLEVEL_SAFE = "1";

    /**
     * @see com.flickr4java.flickr.photos.PhotosInterface#setSafetyLevel(String, String, Boolean)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getSafetyLevel()
     * @see com.flickr4java.flickr.uploader.UploadMetaData#setSafetyLevel(String)
     * @see com.flickr4java.flickr.photos.SearchParameters#setSafeSearch(String)
     */
    public static final String SAFETYLEVEL_MODERATE = "2";

    /**
     * @see com.flickr4java.flickr.photos.PhotosInterface#setSafetyLevel(String, String, Boolean)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getSafetyLevel()
     * @see com.flickr4java.flickr.uploader.UploadMetaData#setSafetyLevel(String)
     * @see com.flickr4java.flickr.photos.SearchParameters#setSafeSearch(String)
     */
    public static final String SAFETYLEVEL_RESTRICTED = "3";

    /**
     * @see com.flickr4java.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_NO_FILTER = 0;

    /**
     * @see com.flickr4java.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_PUBLIC = 1;

    /**
     * @see com.flickr4java.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_FRIENDS = 2;

    /**
     * @see com.flickr4java.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_FAMILY = 3;

    /**
     * @see com.flickr4java.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_FRIENDS_FAMILY = 4;

    /**
     * @see com.flickr4java.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.flickr4java.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_PRIVATE = 5;

    /**
     * Construct a new Flickr gateway instance.
     * 
     * @param apiKey
     *            The API key, must be non-null
     * @param sharedSecret
     * @param transport
     */
    public Flickr(String apiKey, String sharedSecret, Transport transport) {
        setApiKey(apiKey);
        setSharedSecret(sharedSecret);
        setTransport(transport);
    }

    /**
     * Get the API key.
     * 
     * @return The API key
     */
    @Override
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Set the API key to use which must not be null.
     * 
     * @param apiKey
     *            The API key which cannot be null
     */
    @Override
    public void setApiKey(String apiKey) {
        if (apiKey == null) {
            throw new IllegalArgumentException("API key must not be null");
        }
        this.apiKey = apiKey;
    }

    @Override
    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    /**
     * Get the Auth-object.
     * 
     * @return The Auth-object
     */
    @Override
    public Auth getAuth() {
        return auth;
    }

    /**
     * Get the Shared-Secret.
     * 
     * @return The Shared-Secret
     */
    @Override
    public String getSharedSecret() {
        return sharedSecret;
    }

    /**
     * Set the Shared-Secret to use which must not be null.
     * 
     * @param sharedSecret
     *            The Shared-Secret which cannot be null
     */
    @Override
    public void setSharedSecret(String sharedSecret) {
        if (sharedSecret == null) {
            throw new IllegalArgumentException("Shared-Secret must not be null");
        }
        this.sharedSecret = sharedSecret;
    }

    /**
     * Get the Transport interface.
     * 
     * @return The Tranport interface
     */
    @Override
    public Transport getTransport() {
        return transport;
    }

    /**
     * Set the Transport which must not be null.
     * 
     * @param transport
     */
    @Override
    public void setTransport(Transport transport) {
        if (transport == null) {
            throw new IllegalArgumentException("Transport must not be null");
        }
        this.transport = transport;
    }

    /**
     * Get the AuthInterface.
     * 
     * @return The AuthInterface
     */
    @Override
    public AuthInterface getAuthInterface() {
        if (authInterface == null) {
            authInterface = new AuthInterface(apiKey, sharedSecret, transport);
        }
        return authInterface;
    }

    /**
     * Get the ActivityInterface.
     * 
     * @return The ActivityInterface
     */
    @Override
    public ActivityInterface getActivityInterface() {
        if (activityInterface == null) {
            activityInterface = new ActivityInterface(apiKey, sharedSecret, transport);
        }
        return activityInterface;
    }

    @Override
    public synchronized BlogsInterface getBlogsInterface() {
        if (blogsInterface == null) {
            blogsInterface = new BlogsInterface(apiKey, sharedSecret, transport);
        }
        return blogsInterface;
    }

    @Override
    public CommentsInterface getCommentsInterface() {
        if (commentsInterface == null) {
            commentsInterface = new CommentsInterface(apiKey, sharedSecret, transport);
        }
        return commentsInterface;
    }

    @Override
    public CommonsInterface getCommonsInterface() {
        if (commonsInterface == null) {
            commonsInterface = new CommonsInterface(apiKey, sharedSecret, transport);
        }
        return commonsInterface;
    }

    @Override
    public ContactsInterface getContactsInterface() {
        if (contactsInterface == null) {
            contactsInterface = new ContactsInterface(apiKey, sharedSecret, transport);
        }
        return contactsInterface;
    }

    @Override
    public FavoritesInterface getFavoritesInterface() {
        if (favoritesInterface == null) {
            favoritesInterface = new FavoritesInterface(apiKey, sharedSecret, transport);
        }
        return favoritesInterface;
    }

    @Override
    public GeoInterface getGeoInterface() {
        if (geoInterface == null) {
            geoInterface = new GeoInterface(apiKey, sharedSecret, transport);
        }
        return geoInterface;
    }

    @Override
    public GroupsInterface getGroupsInterface() {
        if (groupsInterface == null) {
            groupsInterface = new GroupsInterface(apiKey, sharedSecret, transport);
        }
        return groupsInterface;
    }

    /**
     * @return the interface to the flickr.interestingness methods
     */
    @Override
    public synchronized InterestingnessInterface getInterestingnessInterface() {
        if (interestingnessInterface == null) {
            interestingnessInterface = new InterestingnessInterface(apiKey, sharedSecret, transport);
        }
        return interestingnessInterface;
    }

    @Override
    public LicensesInterface getLicensesInterface() {
        if (licensesInterface == null) {
            licensesInterface = new LicensesInterface(apiKey, sharedSecret, transport);
        }
        return licensesInterface;
    }

    @Override
    public MachinetagsInterface getMachinetagsInterface() {
        if (machinetagsInterface == null) {
            machinetagsInterface = new MachinetagsInterface(apiKey, sharedSecret, transport);
        }
        return machinetagsInterface;
    }

    @Override
    public MembersInterface getMembersInterface() {
        if (membersInterface == null) {
            membersInterface = new MembersInterface(apiKey, sharedSecret, transport);
        }
        return membersInterface;
    }

    @Override
    public NotesInterface getNotesInterface() {
        if (notesInterface == null) {
            notesInterface = new NotesInterface(apiKey, sharedSecret, transport);
        }
        return notesInterface;
    }

    @Override
    public PandaInterface getPandaInterface() {
        if (pandaInterface == null) {
            pandaInterface = new PandaInterface(apiKey, sharedSecret, transport);
        }
        return pandaInterface;
    }

    @Override
    public PoolsInterface getPoolsInterface() {
        if (poolsInterface == null) {
            poolsInterface = new PoolsInterface(apiKey, sharedSecret, transport);
        }
        return poolsInterface;
    }

    @Override
    public PeopleInterface getPeopleInterface() {
        if (peopleInterface == null) {
            peopleInterface = new PeopleInterface(apiKey, sharedSecret, transport);
        }
        return peopleInterface;
    }

    @Override
    public PhotosInterface getPhotosInterface() {
        if (photosInterface == null) {
            photosInterface = new PhotosInterface(apiKey, sharedSecret, transport);
        }
        return photosInterface;
    }

    @Override
    public PhotosetsCommentsInterface getPhotosetsCommentsInterface() {
        if (photosetsCommentsInterface == null) {
            photosetsCommentsInterface = new PhotosetsCommentsInterface(apiKey, sharedSecret, transport);
        }
        return photosetsCommentsInterface;
    }

    @Override
    public PhotosetsInterface getPhotosetsInterface() {
        if (photosetsInterface == null) {
            photosetsInterface = new PhotosetsInterface(apiKey, sharedSecret, transport);
        }
        return photosetsInterface;
    }

    @Override
    public CollectionsInterface getCollectionsInterface() {
        if (collectionsInterface == null) {
            collectionsInterface = new CollectionsInterface(apiKey, sharedSecret, transport);
        }
        return collectionsInterface;
    }

    @Override
    public PlacesInterface getPlacesInterface() {
        if (placesInterface == null) {
            placesInterface = new PlacesInterface(apiKey, sharedSecret, transport);
        }
        return placesInterface;
    }

    @Override
    public PrefsInterface getPrefsInterface() {
        if (prefsInterface == null) {
            prefsInterface = new PrefsInterface(apiKey, sharedSecret, transport);
        }
        return prefsInterface;
    }

    @Override
    public ReflectionInterface getReflectionInterface() {
        if (reflectionInterface == null) {
            reflectionInterface = new ReflectionInterface(apiKey, sharedSecret, transport);
        }
        return reflectionInterface;
    }

    /**
     * Get the TagsInterface for working with Flickr Tags.
     * 
     * @return The TagsInterface
     */
    @Override
    public TagsInterface getTagsInterface() {
        if (tagsInterface == null) {
            tagsInterface = new TagsInterface(apiKey, sharedSecret, transport);
        }
        return tagsInterface;
    }

    @Override
    public TestInterface getTestInterface() {
        if (testInterface == null) {
            testInterface = new TestInterface(apiKey, sharedSecret, transport);
        }
        return testInterface;
    }

    @Override
    public TransformInterface getTransformInterface() {
        if (transformInterface == null) {
            transformInterface = new TransformInterface(apiKey, sharedSecret, transport);
        }
        return transformInterface;
    }

    @Override
    public UploadInterface getUploadInterface() {
        if (uploadInterface == null) {
            uploadInterface = new UploadInterface(apiKey, sharedSecret, transport);
        }
        return uploadInterface;
    }

    @Override
    public Uploader getUploader() {
        if (uploader == null) {
            uploader = new Uploader(apiKey, sharedSecret);
        }
        return uploader;
    }

    @Override
    public UrlsInterface getUrlsInterface() {
        if (urlsInterface == null) {
            urlsInterface = new UrlsInterface(apiKey, sharedSecret, transport);
        }
        return urlsInterface;
    }

    @Override
    public GalleriesInterface getGalleriesInterface() {
        if (galleriesInterface == null) {
            galleriesInterface = new GalleriesInterface(apiKey, sharedSecret, transport);
        }
        return galleriesInterface;
    }

    @Override
    public StatsInterface getStatsInterface() {
        if (statsInterface == null) {
            statsInterface = new StatsInterface(apiKey, sharedSecret, transport);
        }
        return statsInterface;
    }

    @Override
    public CamerasInterface getCamerasInterface() {
        if (cameraInterface == null) {
            cameraInterface = new CamerasInterface(apiKey, sharedSecret, transport);
        }
        return cameraInterface;
    }

    /**
     * Get the SuggestionsInterface.
     * 
     * @return The SuggestionsInterface
     */
    @Override
    public SuggestionsInterface getSuggestionsInterface() {
        if (suggestionsInterface == null) {
            suggestionsInterface = new SuggestionsInterface(apiKey, sharedSecret, transport);
        }
        return suggestionsInterface;
    }

    /**
     * Get the GroupDiscussInterface.
     * 
     * @return The GroupDiscussInterface
     */

    @Override
    public GroupDiscussInterface getDiscussionInterface() {
        if (discussionInterface == null) {
            discussionInterface = new GroupDiscussInterface(apiKey, sharedSecret, transport);
        }
        return discussionInterface;
    }

}
