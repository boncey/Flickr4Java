package com.flickr4java.flickr.photos;

import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.places.Place;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.util.XMLUtilities;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilitiy-methods to transfer requested XML to Photo-objects.
 * 
 * @author till, x-mago
 * @version $Id: PhotoUtils.java,v 1.20 2009/07/23 21:49:35 x-mago Exp $
 */
public final class PhotoUtils {

    private PhotoUtils() {
    }

    /**
     * Try to get an attribute value from two elements.
     * 
     * @param firstElement
     * @param secondElement
     * @return attribute value
     */
    private static String getAttribute(String name, Element firstElement, Element secondElement) {
        String val = firstElement.getAttribute(name);
        if (val.length() == 0 && secondElement != null) {
            val = secondElement.getAttribute(name);
        }
        return val;
    }

    /**
     * Transfer the Information of a photo from a DOM-object to a Photo-object.
     * 
     * @param photoElement
     * @return Photo
     */
    public static final Photo createPhoto(Element photoElement) {
        return createPhoto(photoElement, null);
    }

    /**
     * Transfer the Information of a photo from a DOM-object to a Photo-object.
     * 
     * @param photoElement
     * @param defaultElement
     * @return Photo
     */
    public static final Photo createPhoto(Element photoElement, Element defaultElement) {
        Photo photo = new Photo();
        photo.setId(photoElement.getAttribute("id"));
        photo.setPlaceId(photoElement.getAttribute("place_id"));
        photo.setSecret(photoElement.getAttribute("secret"));
        photo.setServer(photoElement.getAttribute("server"));
        photo.setFarm(photoElement.getAttribute("farm"));
        photo.setRotation(photoElement.getAttribute("rotation"));
        photo.setFavorite("1".equals(photoElement.getAttribute("isfavorite")));
        photo.setLicense(photoElement.getAttribute("license"));
        photo.setOriginalFormat(photoElement.getAttribute("originalformat"));
        photo.setOriginalSecret(photoElement.getAttribute("originalsecret"));
        photo.setIconServer(photoElement.getAttribute("iconserver"));
        photo.setIconFarm(photoElement.getAttribute("iconfarm"));
        photo.setDateTaken(photoElement.getAttribute("datetaken"));
        photo.setDatePosted(photoElement.getAttribute("dateupload"));
        photo.setLastUpdate(photoElement.getAttribute("lastupdate"));
        // flickr.groups.pools.getPhotos provides this value!
        photo.setDateAdded(photoElement.getAttribute("dateadded"));
        photo.setOriginalWidth(photoElement.getAttribute("width_o"));
        photo.setOriginalHeight(photoElement.getAttribute("height_o"));
        photo.setMedia(photoElement.getAttribute("media"));
        photo.setMediaStatus(photoElement.getAttribute("media_status"));
        photo.setPathAlias(photoElement.getAttribute("pathalias"));
        photo.setViews(photoElement.getAttribute("views"));

        Element peopleElement = (Element) photoElement.getElementsByTagName("people").item(0);
        if (peopleElement != null) {
            photo.setIsHasPeople("1".equals(peopleElement.getAttribute("haspeople")));
        } else {
            photo.setIsHasPeople(false);
        }

        // If the attributes active that contain the image-urls,
        // Size-objects created from them, which are used to override
        // the Url-generation.
        List<Size> sizes = new ArrayList<Size>();
        String urlTmp = photoElement.getAttribute("url_t");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.THUMB);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_t"));
            sizeT.setHeight(photoElement.getAttribute("height_t"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_s");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.SMALL);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_s"));
            sizeT.setHeight(photoElement.getAttribute("height_s"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_sq");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.SQUARE);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_sq"));
            sizeT.setHeight(photoElement.getAttribute("height_sq"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_m");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.MEDIUM);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_m"));
            sizeT.setHeight(photoElement.getAttribute("height_m"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_l");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.LARGE);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_l"));
            sizeT.setHeight(photoElement.getAttribute("height_l"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_o");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.ORIGINAL);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_o"));
            sizeT.setHeight(photoElement.getAttribute("height_o"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_q");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.SQUARE_LARGE);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_q"));
            sizeT.setHeight(photoElement.getAttribute("height_q"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_n");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.SMALL_320);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_n"));
            sizeT.setHeight(photoElement.getAttribute("height_n"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_z");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.MEDIUM_640);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_z"));
            sizeT.setHeight(photoElement.getAttribute("height_z"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_c");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.MEDIUM_800);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_c"));
            sizeT.setHeight(photoElement.getAttribute("height_c"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_h");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.LARGE_1600);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_h"));
            sizeT.setHeight(photoElement.getAttribute("height_h"));
            sizes.add(sizeT);
        }
        urlTmp = photoElement.getAttribute("url_k");
        if (urlTmp != null && urlTmp.startsWith("http")) {
            Size sizeT = new Size();
            sizeT.setLabel(Size.LARGE_2048);
            sizeT.setSource(urlTmp);
            sizeT.setWidth(photoElement.getAttribute("width_k"));
            sizeT.setHeight(photoElement.getAttribute("height_k"));
            sizes.add(sizeT);
        }
        if (sizes.size() > 0) {
            photo.setSizes(sizes);
        }

        // Searches, or other list may contain orginal_format.
        // If not choosen via extras, set jpg as default.
        try {
            if (photo.getOriginalFormat() == null || photo.getOriginalFormat().equals("")) {
                String media = photo.getMedia();
                if (media != null && media.equals("video"))
                    photo.setOriginalFormat("mov"); // Currently flickr incorrectly returns original_format as jpg for movies.
                else
                    photo.setOriginalFormat("jpg");
            }
        } catch (NullPointerException e) {
            photo.setOriginalFormat("jpg");
        }

        try {
            Element ownerElement = (Element) photoElement.getElementsByTagName("owner").item(0);
            if (ownerElement == null) {
                User owner = new User();
                owner.setId(getAttribute("owner", photoElement, defaultElement));
                owner.setUsername(getAttribute("ownername", photoElement, defaultElement));
                photo.setOwner(owner);
                photo.setUrl("https://flickr.com/photos/" + owner.getId() + "/" + photo.getId());
            } else {
                User owner = new User();
                owner.setId(ownerElement.getAttribute("nsid"));

                String username = ownerElement.getAttribute("username");
                String ownername = ownerElement.getAttribute("ownername");
                // try to get the username either from the "username" attribute or
                // from the "ownername" attribute
                if (username != null && !"".equals(username)) {
                    owner.setUsername(username);
                } else if (ownername != null && !"".equals(ownername)) {
                    owner.setUsername(ownername);
                }

                owner.setUsername(ownerElement.getAttribute("username"));
                owner.setRealName(ownerElement.getAttribute("realname"));
                owner.setLocation(ownerElement.getAttribute("location"));
                photo.setOwner(owner);
                photo.setUrl("https://flickr.com/photos/" + owner.getId() + "/" + photo.getId());
            }
        } catch (IndexOutOfBoundsException e) {
            User owner = new User();
            owner.setId(photoElement.getAttribute("owner"));
            owner.setUsername(photoElement.getAttribute("ownername"));
            photo.setOwner(owner);
            photo.setUrl("https://flickr.com/photos/" + owner.getId() + "/" + photo.getId());
        }

        try {
            photo.setTitle(XMLUtilities.getChildValue(photoElement, "title"));
            if (photo.getTitle() == null) {
                photo.setTitle(photoElement.getAttribute("title"));
            }
        } catch (IndexOutOfBoundsException e) {
            photo.setTitle(photoElement.getAttribute("title"));
        }

        try {
            photo.setDescription(XMLUtilities.getChildValue(photoElement, "description"));
        } catch (IndexOutOfBoundsException e) {
        }

        try {
            // here the flags are set, if the photo is read by getInfo().
            Element visibilityElement = (Element) photoElement.getElementsByTagName("visibility").item(0);
            photo.setPublicFlag("1".equals(visibilityElement.getAttribute("ispublic")));
            photo.setFriendFlag("1".equals(visibilityElement.getAttribute("isfriend")));
            photo.setFamilyFlag("1".equals(visibilityElement.getAttribute("isfamily")));
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
            // these flags are set here, if photos read from a list.
            photo.setPublicFlag("1".equals(photoElement.getAttribute("ispublic")));
            photo.setFriendFlag("1".equals(photoElement.getAttribute("isfriend")));
            photo.setFamilyFlag("1".equals(photoElement.getAttribute("isfamily")));
        }

        // Parse either photo by getInfo, or from list
        try {
            Element datesElement = XMLUtilities.getChild(photoElement, "dates");
            photo.setDatePosted(datesElement.getAttribute("posted"));
            photo.setDateTaken(datesElement.getAttribute("taken"));
            photo.setTakenGranularity(datesElement.getAttribute("takengranularity"));
            photo.setLastUpdate(datesElement.getAttribute("lastupdate"));
        } catch (IndexOutOfBoundsException e) {
            photo.setDateTaken(photoElement.getAttribute("datetaken"));
        } catch (NullPointerException e) {
            photo.setDateTaken(photoElement.getAttribute("datetaken"));
        }

        try {
            Element permissionsElement = (Element) photoElement.getElementsByTagName("permissions").item(0);
            Permissions permissions = new Permissions();
            permissions.setComment(permissionsElement.getAttribute("permcomment"));
            permissions.setAddmeta(permissionsElement.getAttribute("permaddmeta"));
            photo.setPermissions(permissions);
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
            // nop
        }

        try {
            Element editabilityElement = (Element) photoElement.getElementsByTagName("editability").item(0);
            Editability editability = new Editability();
            editability.setComment("1".equals(editabilityElement.getAttribute("cancomment")));
            editability.setAddmeta("1".equals(editabilityElement.getAttribute("canaddmeta")));
            photo.setEditability(editability);
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
            // nop
        }

        try {
            Element publicEditabilityElement = (Element) photoElement.getElementsByTagName("publiceditability").item(0);
            Editability publicEditability = new Editability();
            publicEditability.setComment("1".equals(publicEditabilityElement.getAttribute("cancomment")));
            publicEditability.setAddmeta("1".equals(publicEditabilityElement.getAttribute("canaddmeta")));
            photo.setPublicEditability(publicEditability);
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
            // nop
        }

        try {
            Element usageElement = (Element) photoElement.getElementsByTagName("usage").item(0);
            Usage usage = new Usage();

            usage.setIsCanBlog("1".equals(usageElement.getAttribute("canblog")));
            usage.setIsCanDownload("1".equals(usageElement.getAttribute("candownload")));
            usage.setIsCanShare("1".equals(usageElement.getAttribute("canshare")));
            usage.setIsCanPrint("1".equals(usageElement.getAttribute("canprint")));
            photo.setUsage(usage);
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
            // nop
        }

        try {
            Element commentsElement = (Element) photoElement.getElementsByTagName("comments").item(0);
            photo.setComments(((Text) commentsElement.getFirstChild()).getData());
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
            // nop
        }

        try {
            Element notesElement = (Element) photoElement.getElementsByTagName("notes").item(0);
            List<Note> notes = new ArrayList<Note>();
            NodeList noteNodes = notesElement.getElementsByTagName("note");
            for (int i = 0; i < noteNodes.getLength(); i++) {
                Element noteElement = (Element) noteNodes.item(i);
                Note note = new Note();
                note.setId(noteElement.getAttribute("id"));
                note.setAuthor(noteElement.getAttribute("author"));
                note.setAuthorName(noteElement.getAttribute("authorname"));
                note.setBounds(noteElement.getAttribute("x"), noteElement.getAttribute("y"), noteElement.getAttribute("w"), noteElement.getAttribute("h"));
                note.setText(noteElement.getTextContent());
                notes.add(note);
            }
            photo.setNotes(notes);
        } catch (IndexOutOfBoundsException e) {
            photo.setNotes(new ArrayList<Note>());
        } catch (NullPointerException e) {
            photo.setNotes(new ArrayList<Note>());
        }

        // Tags coming as space-seperated attribute calling
        // InterestingnessInterface#getList().
        // Through PhotoInterface#getInfo() the Photo has a list of
        // Elements.
        try {
            List<Tag> tags = new ArrayList<Tag>();
            String tagsAttr = photoElement.getAttribute("tags");
            if (!tagsAttr.equals("")) {
                String[] values = tagsAttr.split("\\s+");
                for (int i = 0; i < values.length; i++) {
                    Tag tag = new Tag();
                    tag.setValue(values[i]);
                    tags.add(tag);
                }
            } else {
                try {
                    Element tagsElement = (Element) photoElement.getElementsByTagName("tags").item(0);
                    NodeList tagNodes = tagsElement.getElementsByTagName("tag");
                    for (int i = 0; i < tagNodes.getLength(); i++) {
                        Element tagElement = (Element) tagNodes.item(i);
                        Tag tag = new Tag();
                        tag.setId(tagElement.getAttribute("id"));
                        tag.setAuthor(tagElement.getAttribute("author"));
                        tag.setRaw(tagElement.getAttribute("raw"));
                        tag.setValue(((Text) tagElement.getFirstChild()).getData());
                        tags.add(tag);
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            }
            photo.setTags(tags);
        } catch (NullPointerException e) {
            photo.setTags(new ArrayList<Tag>());
        }

        try {
            Element urlsElement = (Element) photoElement.getElementsByTagName("urls").item(0);
            List<String> urls = new ArrayList<String>();
            NodeList urlNodes = urlsElement.getElementsByTagName("url");
            for (int i = 0; i < urlNodes.getLength(); i++) {
                Element urlElement = (Element) urlNodes.item(i);
                PhotoUrl photoUrl = new PhotoUrl();
                photo.setPhotoUrl(photoUrl);
                photoUrl.setType(urlElement.getAttribute("type"));
                photoUrl.setUrl(XMLUtilities.getValue(urlElement));
                if (photoUrl.getType().equals("photopage")) {
                    photo.setUrl(photoUrl.getUrl());
                    urls.add(photoUrl.getUrl());
                }

            }

            photo.setUrls(urls);
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
            photo.setUrls(new ArrayList<String>());
        }

        String longitude = null;
        String latitude = null;
        String accuracy = null;
        try {
            Element geoElement = (Element) photoElement.getElementsByTagName("location").item(0);
            longitude = geoElement.getAttribute("longitude");
            latitude = geoElement.getAttribute("latitude");
            accuracy = geoElement.getAttribute("accuracy");
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
            // Geodata may be available as attributes in the photo-tag itself!
            try {
                longitude = photoElement.getAttribute("longitude");
                latitude = photoElement.getAttribute("latitude");
                accuracy = photoElement.getAttribute("accuracy");
            } catch (NullPointerException e2) {
                // no geodata at all
            }
        }
        if (longitude != null && latitude != null) {
            if (longitude.length() > 0 && latitude.length() > 0 && !("0".equals(longitude) && "0".equals(latitude))) {
                photo.setGeoData(new GeoData(longitude, latitude, accuracy));
            }
        }

        try {
            Place place = null;
            Element element = (Element) photoElement.getElementsByTagName("locality").item(0);
            place = new Place(element.getAttribute("place_id"), element.getTextContent(), element.getAttribute("woeid"));
            photo.setLocality(place);
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }

        try {
            Place place = null;
            Element element = (Element) photoElement.getElementsByTagName("county").item(0);
            place = new Place(element.getAttribute("place_id"), element.getTextContent(), element.getAttribute("woeid"));
            photo.setCounty(place);
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }

        try {
            Place place = null;
            Element element = (Element) photoElement.getElementsByTagName("region").item(0);
            place = new Place(element.getAttribute("place_id"), element.getTextContent(), element.getAttribute("woeid"));
            photo.setRegion(place);
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }

        try {
            Place place = null;
            Element element = (Element) photoElement.getElementsByTagName("country").item(0);
            place = new Place(element.getAttribute("place_id"), element.getTextContent(), element.getAttribute("woeid"));
            photo.setCountry(place);
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }

        return photo;
    }

    /**
     * Parse a list of Photos from given Element.
     * 
     * @param photosElement
     * @return PhotoList
     */
    public static final PhotoList<Photo> createPhotoList(Element photosElement) {
        PhotoList<Photo> photos = new PhotoList<Photo>();
        photos.setPage(photosElement.getAttribute("page"));
        photos.setPages(photosElement.getAttribute("pages"));
        photos.setPerPage(photosElement.getAttribute("perpage"));
        photos.setTotal(photosElement.getAttribute("total"));

        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }

}
