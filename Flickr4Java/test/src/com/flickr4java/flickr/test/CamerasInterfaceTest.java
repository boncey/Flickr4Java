package com.flickr4java.flickr.test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.cameras.Brand;
import com.flickr4java.flickr.cameras.Camera;
import com.flickr4java.flickr.cameras.CamerasInterface;

import org.junit.Test;

import java.util.Collection;

public class CamerasInterfaceTest extends Flickr4JavaTest {

    @Test
    public void testGetBrands() throws FlickrException {

        // not implementing much for tests here - flickr doesn't give us much to go on.
        CamerasInterface camInterface = flickr.getCamerasInterface();
        Collection<Brand> brands = camInterface.getBrands();
        assertNotNull(brands);
        assertTrue(brands.size() > 0);
    }

    @Test
    public void testGetBrandModels() throws FlickrException {

        // not implementing much for tests here - flickr doesn't give us much to go on.
        // the interface may not be final - the documented xml nodes don't correspond to what is in the xml
        CamerasInterface camInterface = flickr.getCamerasInterface();
        Flickr.debugStream = true;
        Collection<Camera> cams = camInterface.getBrandModels("Canon");
        assertNotNull(cams);
        assertTrue(cams.size() > 0);
        for (Camera cam : cams) {
            assertNotNull(cam.getId());
            assertNotNull(cam.getName());
            /*
             * assertNotNull(cam.getSmallImage()); assertNotNull(cam.getLargeImage()); assertNotNull(cam.getDetails().getMegapixels());
             * assertNotNull(cam.getDetails().getZoom()); assertNotNull(cam.getDetails().getLcdSize()); assertNotNull(cam.getDetails().getStorageType());
             */}
    }

}
