## Flickr4Java

### Introduction

__Note:__ This API has been forked from [FlickrJ at Sourceforge](http://flickrj.sourceforge.net/).

This is a Java API which wraps the [REST-based Flickr API](http://www.flickr.com/services/api/).

Comments and questions should be sent to the [GitHub Repo](https://github.com/callmeal/Flickr4Java).

### Usage

To use the API just construct an instance of the class `com.flickr4java.flickr.test.Flickr` and request the interfaces which you need to work with.  
For example, to send a test ping to the Flickr service:

    String apiKey = "YOUR_API_KEY";
    String sharedSecret = "YOUR_SHARED_SECRET";
    Flickr f = new Flickr(apiKey, sharedSecret, new REST());
    TestInterface testInterface = f.getTestInterface();
    Collection results = testInterface.echo(Collections.EMPTY_MAP);

__Please note:__ this library is not thread safe.

###Setup for gradle
```gradle
  compile 'com.aetrion.flickr:flickrapi:1.1'
  ```

### Requirements

This API has been tested and built with JDK 1.8.

An API key is required to use this API.  You can [request one on Flickr](http://www.flickr.com/services/api/).

#### Required libraries

- [scribe-java (v.1.3.2 onwards)](https://github.com/fernandezpablo85/scribe-java/wiki/Getting-Started) (required for the OAuth functionality).
- [log4j](http://www.apache.org/dyn/closer.cgi/logging/log4j/1.2.17/log4j-1.2.17.zip) (runtime dependency for logging)

### Download

[Download the latest version from bintray](https://bintray.com/boncey/Flickr4Java/Flickr4Java).


### Maven

    <dependency>
      <groupId>com.flickr4java</groupId>
      <artifactId>flickr4java</artifactId>
      <version>2.18</version>
    </dependency>

Flickr4Java is now available on Maven Central so the above settings should be all you need (it used to only be available on [JCenter](https://bintray.com/bintray/jcenter).

