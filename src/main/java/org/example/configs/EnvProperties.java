package org.example.configs;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvProperties {

    private static Properties instance;

    private EnvProperties() {}

    public static Properties getInstance() {
        if (instance == null) {
            Properties props = new Properties();
            try {
                InputStream input = EnvProperties.class.getResourceAsStream("/env.properties");
                props.load(input);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            instance = props;
        }
        return instance;
    }
}
