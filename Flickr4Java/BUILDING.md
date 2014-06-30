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

To test:

1. Copy `src/test/resources/setup.properties.example` to `src/test/resources/setup.properties`
2. Run `gradle compiletestjava -q` — compile all sources
3. Run `gradle setuptests -q` — this will prompt for authorisation
   and update the `setup.properties` file with the test user's details
4. Run `gradle test` — can be run as `gradle -Dtest.single=NameOfTest test` to
   run only one test (because the full suite can take quite a while)

(The `-q` above just hides some of the more verbose output; it can be left out.)

Most of the tests are integration tests and require hitting the actual Flickr
API service with DELETE permissions. The safest and easiest way to do this is to
set up a test user account with the following:

1.  At least one photo with a location set, in at least one album, with exactly 3 tags.
    Add this photo's ID as `photoid` and `geo.write.photoid` in `setup.properties`.
2.  At least one collection with title and description, containing at least one album
    (the ID of the collection should be retrieved manually via the
    [API explorer](https://www.flickr.com/services/api/explore/flickr.collections.getTree)
    and be added as `collectionid` in `setup.properties`).
3.  At least one comment on another user's photo.
4.  Following at least one other user.
5.  At least one favorite.
6.  Allows 'anyone' "to see your stuff on a map".
7.  A member of at least one group, whose ID is saved as `testgroupid`.
8.  A gallery, containing at least one photo.
9.  An "[own Flickr address](https://www.flickr.com/profile_url.gne)" set to `username` (see below).
10. *[List is incomplete]*

The last few keys in the `setup.properties` need to be as follows:

* Set the following keys in `setup.properties` to match your test user:
  `nsid`, `username`, `displayname`, and `email`.
* Point `imagefile` to a test image that will be uploaded (and then deleted).
