package org.example.api.core;

import org.example.configs.EnvironmentConfig;

/*
Класс, хранящий основной URL и эндпоинты API
 */
public class Endpoints {

    private static final String BASE_URL = EnvironmentConfig.getBaseUrl();

    public static final String LOGIN = BASE_URL + "user/login";
    public static final String USER = BASE_URL + "user/";
}
