# Building and Testing Flickr4Java

## Building

Flickr4Java uses [Gradle](http://www.gradle.org).
Run gradle tasks from the directory that contains the `build.gradle` file.

The following tasks are available:

* Clean up:                        `gradle clean`
* Compile (inc examples):          `gradle compileJava`
* Compile & test:                  `gradle build`
* Create docs:                     `gradle javadoc`
* Create docs jar:                 `gradle javadocJar`
* Create src jar:                  `gradle sourcesJar`
* Generate Eclipse project files:  `gradle cleanEclipse eclipse`
* Generate Idea project files:     `gradle cleanIdea idea`
* Show dependencies (libs etc):    `gradle dependencies`

## Testing

Most of the tests are integration tests, and require hitting the actual Flickr
API service with DELETE permissions. The safest and easiest way to do this is to
set up a test user account, the following photos etc.:

1. At least one photo in one album
2. At least one collection with title and description, containing at least one album
   (the ID of the collection can be retrieved manually via the
   [API explorer](https://www.flickr.com/services/api/explore/flickr.collections.getTree)
   and be added as `collectionid` in `src/test/resources/setup.properties`)
3. *[List is incomplete]*

To test:

1. Copy `src/test/resources/setup.properties.example` to `src/test/resources/setup.properties`
2. Run `gradle compiletestjava -q` — compile all sources
3. Run `gradle setuptests -q` — this will prompt for authorisation and update the above `setup.properties` file
4. Run `gradle test`

(The `-q` above just hides some of the more verbose output; it can be left out.)
