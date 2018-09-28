/*
 * FontDecoder.java
 * Copyright (c) 2005-2017 Radek Burget
 *
 * CSSBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * CSSBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with CSSBox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Created on 1. 11. 2017, 23:23:03 by burgetr
 */
package org.fit.cssbox.css;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.fit.cssbox.io.DocumentSource;


/**
 *
 * @author burgetr
 */
public class FontDecoder
{
    public static List<String> supportedFormats;
    static {
        supportedFormats = new ArrayList<String>(1);
        supportedFormats.add("truetype");
    }
    
    private static FontRegistry fontRegistry = new HashMapFontRegistry();

    public static void registerFont(URL url, String family)
    {
        fontRegistry.registerFont(url, family);
    }
    
    public static String findRegisteredFont(URL url)
    {
        return fontRegistry.findRegisteredFont(url);
    }
    
    public static Font decodeFont(DocumentSource fontSource, String format) throws FontFormatException, IOException
    {
        return fontRegistry.decodeFont(fontSource, format);
    }


    public static void setFontRegistry(FontRegistry registry) {
        FontDecoder.fontRegistry = registry;
    }

}
