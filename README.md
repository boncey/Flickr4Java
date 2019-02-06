## Flickr4Java

### Introduction

__Note:__ This API has been forked from [FlickrJ at Sourceforge](http://flickrj.sourceforge.net/).

This is a Java API which wraps the [REST-based Flickr API](http://www.flickr.com/services/api/).

Comments and questions should be raised on the [GitHub Repo issue tracker](https://github.com/boncey/Flickr4Java/issues).

### Usage

To use the API just construct an instance of the class `com.flickr4java.flickr.test.Flickr` and request the interfaces which you need to work with.  
For example, to send a test ping to the Flickr service:

    String apiKey = "YOUR_API_KEY";
    String sharedSecret = "YOUR_SHARED_SECRET";
    Flickr f = new Flickr(apiKey, sharedSecret, new REST());
    TestInterface testInterface = f.getTestInterface();
    Collection results = testInterface.echo(Collections.EMPTY_MAP);
    
See `/src/examples/java` for more.

### Requirements

This API has been tested and built with JDK 1.8.

An API key is required to use this API.  You can [request one on Flickr](http://www.flickr.com/services/api/).

#### Required libraries

- [scribejava-api (v 6.2.0 onwards)](https://github.com/scribejava/scribejava) (required for the OAuth functionality).
- [slf4j](https://www.slf4j.org) (runtime dependency for logging)

### Download

[Download the latest version from bintray](https://bintray.com/boncey/Flickr4Java/Flickr4Java).


### Gradle

    compile 'com.flickr4java:flickr4java:2.19'

### Maven

    <dependency>
      <groupId>com.flickr4java</groupId>
      <artifactId>flickr4java</artifactId>
      <version>2.19</version>
    </dependency>

Flickr4Java is available on Maven Central so the above settings should be all you need.

### Testing
The tests now run against captured responses from Flickr (see `src/test/resources/payloads`) and don't contact the Flickr API at all.  
This means there is no longer any need to create a test account and populate a properties file.
