package com.flickr4java.flickr.json;

import org.apache.log4j.Logger;
import org.scribe.model.Response;

import com.flickr4java.flickr.JSONResponse;
import com.flickr4java.flickr.util.StringUtilities;

/**
 *
 */
public class JSONResponseImpl implements JSONResponse {

    private static final Logger logger = Logger.getLogger(JSONResponseImpl.class);


    private Response scribeResponse;
    private StringBuilder body;

    public JSONResponseImpl(Response scribeResponse){
        this.scribeResponse = scribeResponse;
        if(StringUtilities.isNotBlank(scribeResponse.getBody())){
            body = new StringBuilder(scribeResponse.getBody());
        }else{
            body = new StringBuilder("");
        }
    }

    @Override
    public StringBuilder getBody() {
        return body;
    }


}
