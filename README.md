Flickr4Java
==

Introduction
===

__Note:__ This API has been forked from [FlickrJ at Sourceforge](http://flickrj.sourceforge.net/).

This is a Java API which wraps the [REST-based Flickr API](http://www.flickr.com/services/api/).

Comments and questions should be sent to the [GitHub Repo](https://github.com/callmeal/Flickr4Java).

Usage
===

To use the API just construct an instance of the class `com.flickr4java.flickr.test.Flickr` and request the interfaces which you need to work with.  
For example, to send a test ping to the Flickr service:

    String apiKey = "YOUR_API_KEY";
    Flickr f = new Flickr(apiKey);
    TestInterface testInterface = f.getTestInterface();
    Collection results = testInterface.echo(Collections.EMPTY_LIST);

__Please note:__ this library is not thread safe.


Requirements
===
This API has been tested with JDK 1.4 and JDK 1.5. The default distribution is build with JDK 1.5.

An API key is required to use this API.  You can [request one on Flickr](http://www.flickr.com/services/api/).

Download
===

See [Downloads Page](https://github.com/callmeal/Flickr4Java/downloads) for latest release.
