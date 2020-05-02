package com.flickr4java.flickr.photos.upload;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Checks the status of asynchronous photo upload tickets.
 * 
 * @author till (Till Krech) extranoise:flickr
 * @version $Id: UploadInterface.java,v 1.3 2008/01/28 23:01:45 x-mago Exp $
 */
public class UploadInterface {
    public static final String METHOD_CHECK_TICKETS = "flickr.photos.upload.checkTickets";

    private String apiKey;

    private String sharedSecret;

    private Transport transportAPI;

    public UploadInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
    }

    /**
     * Checks the status of one or more asynchronous photo upload tickets. This method does not require authentication.
     * 
     * @param tickets
     *            a set of ticket ids (Strings) or {@link Ticket} objects containing ids
     * @return a list of {@link Ticket} objects.
     * @throws FlickrException if there was a problem connecting to Flickr
     */
    public List<Ticket> checkTickets(Set<String> tickets) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_CHECK_TICKETS);

        StringBuffer sb = new StringBuffer();
        Iterator<String> it = tickets.iterator();
        while (it.hasNext()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            Object obj = it.next();
            if (obj instanceof Ticket) {
                sb.append(((Ticket) obj).getTicketId());
            } else {
                sb.append(obj);
            }
        }
        parameters.put("tickets", sb.toString());

        Response response = transportAPI.post(transportAPI.getPath(), parameters, apiKey, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        // <uploader>
        // <ticket id="128" complete="1" photoid="2995" />
        // <ticket id="129" complete="0" />
        // <ticket id="130" complete="2" />
        // <ticket id="131" invalid="1" />
        // </uploader>

        List<Ticket> list = new ArrayList<Ticket>();
        Element uploaderElement = response.getPayload();
        NodeList ticketNodes = uploaderElement.getElementsByTagName("ticket");
        int n = ticketNodes.getLength();
        for (int i = 0; i < n; i++) {
            Element ticketElement = (Element) ticketNodes.item(i);
            String id = ticketElement.getAttribute("id");
            String complete = ticketElement.getAttribute("complete");
            boolean invalid = "1".equals(ticketElement.getAttribute("invalid"));
            String photoId = ticketElement.getAttribute("photoid");
            Ticket info = new Ticket();
            info.setTicketId(id);
            info.setInvalid(invalid);
            info.setStatus(Integer.parseInt(complete));
            info.setPhotoId(photoId);
            list.add(info);
        }
        return list;
    }

}
