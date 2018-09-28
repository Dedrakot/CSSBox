package org.fit.cssbox.css;

import org.fit.cssbox.io.DocumentSource;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public interface FontRegistry {
    void registerFont(URL url, String family);
    String findRegisteredFont(URL url);

    Font decodeFont(DocumentSource fontSource, String format) throws IOException, FontFormatException;
}
