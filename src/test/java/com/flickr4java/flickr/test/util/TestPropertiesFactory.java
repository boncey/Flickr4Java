package com.flickr4java.flickr.test.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

public class TestPropertiesFactory {

    /**
     * Logger for log4j.
     */
    private static Logger _log = Logger.getLogger(TestPropertiesFactory.class);


    public static TestProperties getTestProperties() {

        TestPropertiesFactory factory = new TestPropertiesFactory();
        Optional<File> fileOptional = factory.findPropertyFile();

        TestProperties testProperties;
        if (fileOptional.isPresent()) {
            testProperties = new FileTestProperties(fileOptional.get());
        } else {
            testProperties = new StubTestProperties();
        }

        return testProperties;
    }

    private Optional<File> findPropertyFile() {
        Optional<File> fileOptional;
        String setupPropertiesPath = System.getenv("SETUP_PROPERTIES_PATH");

        try {
            if (setupPropertiesPath != null) {
                _log.debug("Using properties file at " + setupPropertiesPath);
                File properties = new File(setupPropertiesPath);
                fileOptional = properties.exists() ? Optional.of(properties) : Optional.empty();
            } else {
                _log.debug("Using properties file /setup.properties from classpath");
                File properties =  Paths.get(this.getClass().getResource("/setup.properties").toURI()).toFile();
                fileOptional = properties.exists() ? Optional.of(properties) : Optional.empty();
            }
        } catch (Exception e) {
            _log.warn("Unable to load properties file", e);
            fileOptional = Optional.empty();
        }

        return fileOptional;
    }

}
