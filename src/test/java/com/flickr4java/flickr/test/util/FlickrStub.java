
package com.flickr4java.flickr.test.util;

import com.flickr4java.flickr.IFlickr;
import com.flickr4java.flickr.Transport;
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
import com.flickr4java.flickr.urls.UrlsInterface;

/**
 * Stubbed entry point for the Flickr4Java API. This class is used to acquire Interface classes which wrap the Flickr API.
 *
 */
public class FlickrStub implements IFlickr {

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
     * Construct a new stubbed Flickr instance.
     *
     */
    public FlickrStub() {
        transport = new TransportStub();
    }

    /**
     * Get the API key.
     * 
     * @return The API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Set the API key to use which must not be null.
     * 
     * @param apiKey
     *            The API key which cannot be null
     */
    public void setApiKey(String apiKey) {
        if (apiKey == null) {
            throw new IllegalArgumentException("API key must not be null");
        }
        this.apiKey = apiKey;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    /**
     * Get the Auth-object.
     * 
     * @return The Auth-object
     */
    public Auth getAuth() {
        return auth;
    }

    /**
     * Get the Shared-Secret.
     * 
     * @return The Shared-Secret
     */
    public String getSharedSecret() {
        return sharedSecret;
    }

    /**
     * Set the Shared-Secret to use which must not be null.
     * 
     * @param sharedSecret
     *            The Shared-Secret which cannot be null
     */
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
    public Transport getTransport() {
        return transport;
    }

    /**
     * Set the Transport which must not be null.
     * 
     * @param transport
     */
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
    public ActivityInterface getActivityInterface() {
        if (activityInterface == null) {
            activityInterface = new ActivityInterface(apiKey, sharedSecret, transport);
        }
        return activityInterface;
    }

    public BlogsInterface getBlogsInterface() {
        if (blogsInterface == null) {
            blogsInterface = new BlogsInterface(apiKey, sharedSecret, transport);
        }
        return blogsInterface;
    }

    public CommentsInterface getCommentsInterface() {
        if (commentsInterface == null) {
            commentsInterface = new CommentsInterface(apiKey, sharedSecret, transport);
        }
        return commentsInterface;
    }

    public CommonsInterface getCommonsInterface() {
        if (commonsInterface == null) {
            commonsInterface = new CommonsInterface(apiKey, sharedSecret, transport);
        }
        return commonsInterface;
    }

    public ContactsInterface getContactsInterface() {
        if (contactsInterface == null) {
            contactsInterface = new ContactsInterface(apiKey, sharedSecret, transport);
        }
        return contactsInterface;
    }

    public FavoritesInterface getFavoritesInterface() {
        if (favoritesInterface == null) {
            favoritesInterface = new FavoritesInterface(apiKey, sharedSecret, transport);
        }
        return favoritesInterface;
    }

    public GeoInterface getGeoInterface() {
        if (geoInterface == null) {
            geoInterface = new GeoInterface(apiKey, sharedSecret, transport);
        }
        return geoInterface;
    }

    public GroupsInterface getGroupsInterface() {
        if (groupsInterface == null) {
            groupsInterface = new GroupsInterface(apiKey, sharedSecret, transport);
        }
        return groupsInterface;
    }

    /**
     * @return the interface to the flickr.interestingness methods
     */
    public InterestingnessInterface getInterestingnessInterface() {
        if (interestingnessInterface == null) {
            interestingnessInterface = new InterestingnessInterface(apiKey, sharedSecret, transport);
        }
        return interestingnessInterface;
    }

    public LicensesInterface getLicensesInterface() {
        if (licensesInterface == null) {
            licensesInterface = new LicensesInterface(apiKey, sharedSecret, transport);
        }
        return licensesInterface;
    }

    public MachinetagsInterface getMachinetagsInterface() {
        if (machinetagsInterface == null) {
            machinetagsInterface = new MachinetagsInterface(apiKey, sharedSecret, transport);
        }
        return machinetagsInterface;
    }

    public MembersInterface getMembersInterface() {
        if (membersInterface == null) {
            membersInterface = new MembersInterface(apiKey, sharedSecret, transport);
        }
        return membersInterface;
    }

    public NotesInterface getNotesInterface() {
        if (notesInterface == null) {
            notesInterface = new NotesInterface(apiKey, sharedSecret, transport);
        }
        return notesInterface;
    }

    public PandaInterface getPandaInterface() {
        if (pandaInterface == null) {
            pandaInterface = new PandaInterface(apiKey, sharedSecret, transport);
        }
        return pandaInterface;
    }

    public PoolsInterface getPoolsInterface() {
        if (poolsInterface == null) {
            poolsInterface = new PoolsInterface(apiKey, sharedSecret, transport);
        }
        return poolsInterface;
    }

    public PeopleInterface getPeopleInterface() {
        if (peopleInterface == null) {
            peopleInterface = new PeopleInterface(apiKey, sharedSecret, transport);
        }
        return peopleInterface;
    }

    public PhotosInterface getPhotosInterface() {
        if (photosInterface == null) {
            photosInterface = new PhotosInterface(apiKey, sharedSecret, transport);
        }
        return photosInterface;
    }

    public PhotosetsCommentsInterface getPhotosetsCommentsInterface() {
        if (photosetsCommentsInterface == null) {
            photosetsCommentsInterface = new PhotosetsCommentsInterface(apiKey, sharedSecret, transport);
        }
        return photosetsCommentsInterface;
    }

    public PhotosetsInterface getPhotosetsInterface() {
        if (photosetsInterface == null) {
            photosetsInterface = new PhotosetsInterface(apiKey, sharedSecret, transport);
        }
        return photosetsInterface;
    }

    public CollectionsInterface getCollectionsInterface() {
        if (collectionsInterface == null) {
            collectionsInterface = new CollectionsInterface(apiKey, sharedSecret, transport);
        }
        return collectionsInterface;
    }

    public PlacesInterface getPlacesInterface() {
        if (placesInterface == null) {
            placesInterface = new PlacesInterface(apiKey, sharedSecret, transport);
        }
        return placesInterface;
    }

    public PrefsInterface getPrefsInterface() {
        if (prefsInterface == null) {
            prefsInterface = new PrefsInterface(apiKey, sharedSecret, transport);
        }
        return prefsInterface;
    }

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
    public TagsInterface getTagsInterface() {
        if (tagsInterface == null) {
            tagsInterface = new TagsInterface(apiKey, sharedSecret, transport);
        }
        return tagsInterface;
    }

    public TestInterface getTestInterface() {
        if (testInterface == null) {
            testInterface = new TestInterface(apiKey, sharedSecret, transport);
        }
        return testInterface;
    }

    public TransformInterface getTransformInterface() {
        if (transformInterface == null) {
            transformInterface = new TransformInterface(apiKey, sharedSecret, transport);
        }
        return transformInterface;
    }

    public UploadInterface getUploadInterface() {
        if (uploadInterface == null) {
            uploadInterface = new UploadInterface(apiKey, sharedSecret, transport);
        }
        return uploadInterface;
    }

    public Uploader getUploader() {
        if (uploader == null) {
            Transport uploadTransport = new TransportStub();
            uploader = new Uploader(apiKey, sharedSecret, uploadTransport);
        }
        return uploader;
    }

    public UrlsInterface getUrlsInterface() {
        if (urlsInterface == null) {
            urlsInterface = new UrlsInterface(apiKey, sharedSecret, transport);
        }
        return urlsInterface;
    }

    public GalleriesInterface getGalleriesInterface() {
        if (galleriesInterface == null) {
            galleriesInterface = new GalleriesInterface(apiKey, sharedSecret, transport);
        }
        return galleriesInterface;
    }

    public StatsInterface getStatsInterface() {
        if (statsInterface == null) {
            statsInterface = new StatsInterface(apiKey, sharedSecret, transport);
        }
        return statsInterface;
    }

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

    public GroupDiscussInterface getDiscussionInterface() {
        if (discussionInterface == null) {
            discussionInterface = new GroupDiscussInterface(apiKey, sharedSecret, transport);
        }
        return discussionInterface;
    }

}
