package com.foretell.util;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Singleton
@Slf4j
public class PropertyService {
    public String getMessageProperty(String name) {
        return loadProperties("message.properties").getProperty(name);
    }

    private Properties loadProperties(String propertyFile) {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(propertyFile);
        Properties properties = new Properties();

        try {
            properties.load(inputStream);
        } catch (Exception e) {
            log.info("Can't load {} file", propertyFile);
        }

        return properties;
    }
}
