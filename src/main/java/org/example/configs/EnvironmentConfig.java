package org.example.configs;

import java.util.Properties;

public class EnvironmentConfig {

    private EnvironmentConfig() {}

    private static final Properties properties = EnvProperties.getInstance();

    public static String getBaseUrl() {
        String devUrl = "https://playground.learnqa.ru/api_dev/";
        String prodUrl = "https://playground.learnqa.ru/api/";
        String envMode = properties.getProperty("ENVIRONMENT");

        switch (envMode) {
            case ("dev"):
                return devUrl;
            case ("prod"):
                return prodUrl;
        }
        return null;
    }
}
