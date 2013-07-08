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
This API has been tested with JDK 1.4 and JDK 1.5. The default distribution is built with JDK 1.5 (it should build under 1.6 and 1.7 too though).

An API key is required to use this API.  You can [request one on Flickr](http://www.flickr.com/services/api/).

The [scribe-java jar](https://github.com/downloads/fernandezpablo85/scribe-java/scribe-1.3.0.jar) is also required for the OAuth functionality.

Download
===

[Download the latest version](https://github.com/callmeal/Flickr4Java/archive/master.zip).
