package org.fit.cssbox.css;

import java.net.URL;
import java.util.HashMap;

public class HashMapUrlRegistry implements UrlRegistry {

    private final HashMap<URL, String> registeredFonts = new HashMap<>();

    @Override
    public void registerFont(URL url, String family) {
        registeredFonts.put(url, family);
    }

    @Override
    public String findRegisteredFont(URL url) {
        return registeredFonts.get(url);
    }
}
