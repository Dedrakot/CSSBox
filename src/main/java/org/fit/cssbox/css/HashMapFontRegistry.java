package org.fit.cssbox.css;

import org.fit.cssbox.io.DocumentSource;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class HashMapFontRegistry implements FontRegistry {

    private final HashMap<URL, String> registeredFonts = new HashMap<>();

    @Override
    public void registerFont(URL url, String family) {
        registeredFonts.put(url, family);
    }

    @Override
    public String findRegisteredFont(URL url) {
        return registeredFonts.get(url);
    }

    @Override
    public Font decodeFont(DocumentSource fontSource, String format) throws IOException, FontFormatException {
        //TODO decode other formats than TTF
        return Font.createFont(Font.TRUETYPE_FONT, fontSource.getInputStream());
    }
}
