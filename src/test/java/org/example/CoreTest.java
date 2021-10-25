package org.example;

import org.junit.jupiter.api.BeforeAll;

import java.io.File;

public class CoreTest {

    public static final String FEATURE_USER_AUTH = "Аутентификация пользователя";
    public static final String FEATURE_USER_CREATE = "Создание пользователя";
    public static final String FEATURE_HELLO = "Получение 'Привет'";

    private static Boolean isResultFolderClear = false;

    @BeforeAll
    public static void clearAllureResults() {
        File resultsFolder = new File("allure-results");

        if (isResultFolderClear) {
            return;
        }

        if(resultsFolder.exists()) {
            for(File file : resultsFolder.listFiles()) {
                file.delete();
            }

            setResultFolderClear();
        }
    }

    private static void setResultFolderClear() {
        isResultFolderClear = true;
    }
}
