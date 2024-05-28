package by.aurorasoft.distanceclassifier.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtil {

    public static String append(String baseUrl, String relative) {
        try {
            URL base = new URL(baseUrl);
            return new URL(base, relative).toString();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
