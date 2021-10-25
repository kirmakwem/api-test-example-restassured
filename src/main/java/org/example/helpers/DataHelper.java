package org.example.helpers;

import java.util.Date;

public class DataHelper {

    // Генерация уникального email на основе текущего timestamp
    public String generateUniqueEmail() {
        long currentTime = new Date().getTime();
        return currentTime + "@some.com";
    }
}
