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
import com.flickr4java.flickr.urls.UrlsInterface;

public interface IFlickr {
    String getApiKey();

    void setApiKey(String apiKey);

    void setAuth(Auth auth);

    Auth getAuth();

    String getSharedSecret();

    void setSharedSecret(String sharedSecret);

    Transport getTransport();

    void setTransport(Transport transport);

    AuthInterface getAuthInterface();

    ActivityInterface getActivityInterface();

    BlogsInterface getBlogsInterface();

    CommentsInterface getCommentsInterface();

    CommonsInterface getCommonsInterface();

    ContactsInterface getContactsInterface();

    FavoritesInterface getFavoritesInterface();

    GeoInterface getGeoInterface();

    GroupsInterface getGroupsInterface();

    InterestingnessInterface getInterestingnessInterface();

    LicensesInterface getLicensesInterface();

    MachinetagsInterface getMachinetagsInterface();

    MembersInterface getMembersInterface();

    NotesInterface getNotesInterface();

    PandaInterface getPandaInterface();

    PoolsInterface getPoolsInterface();

    PeopleInterface getPeopleInterface();

    PhotosInterface getPhotosInterface();

    PhotosetsCommentsInterface getPhotosetsCommentsInterface();

    PhotosetsInterface getPhotosetsInterface();

    CollectionsInterface getCollectionsInterface();

    PlacesInterface getPlacesInterface();

    PrefsInterface getPrefsInterface();

    ReflectionInterface getReflectionInterface();

    TagsInterface getTagsInterface();

    TestInterface getTestInterface();

    TransformInterface getTransformInterface();

    UploadInterface getUploadInterface();

    Uploader getUploader();

    UrlsInterface getUrlsInterface();

    GalleriesInterface getGalleriesInterface();

    StatsInterface getStatsInterface();

    CamerasInterface getCamerasInterface();

    SuggestionsInterface getSuggestionsInterface();

    GroupDiscussInterface getDiscussionInterface();
}
