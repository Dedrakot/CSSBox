package org.fit.cssbox.css;

import java.net.URL;

public interface UrlRegistry {
    void registerFont(URL url, String family);
    String findRegisteredFont(URL url);
}
