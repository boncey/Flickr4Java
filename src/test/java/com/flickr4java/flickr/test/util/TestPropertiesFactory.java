package com.flickr4java.flickr.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;

public class TestPropertiesFactory {

    /**
     * Logger.
     */
    private static Logger _log = LoggerFactory.getLogger(TestPropertiesFactory.class);


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
        Optional<File> fileOptional = Optional.empty();
        String setupPropertiesPath = System.getenv("SETUP_PROPERTIES_PATH");

        try {
            if (setupPropertiesPath != null && setupPropertiesPath.length() > 0) {
                _log.info("Using properties file at '{}'", setupPropertiesPath);
                File properties = new File(setupPropertiesPath);
                fileOptional = properties.exists() ? Optional.of(properties) : Optional.empty();
            } else {
                URL resource = this.getClass().getResource("/setup.properties");
                if (resource != null) {
                    fileOptional = Optional.of(Paths.get(resource.toURI()).toFile());
                    _log.info("Using properties file /setup.properties from classpath");
                }
            }
        } catch (Exception e) {
            _log.warn("Unable to load properties file", e);
            fileOptional = Optional.empty();
        }

        return fileOptional;
    }

}
