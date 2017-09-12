package org.fit.cssbox.testapp;

import cz.vutbr.web.css.MediaSpec;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;
import org.fit.cssbox.layout.BrowserCanvas;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author gladyshev
 */
public class TemplateRendererTestApp {

    private boolean loadImages;
    private boolean loadBackgroundImages;
    private int timeOut;
    private boolean cropWindow;


    public TemplateRendererTestApp(boolean loadImages, boolean loadBackgroundImages, int timeOut, boolean cropWindow) {
        this.cropWindow = cropWindow;
        this.loadImages = loadImages;
        this.loadBackgroundImages = loadBackgroundImages;
        this.timeOut = timeOut;
    }

    public static void main(String[] args) throws IOException, SAXException {
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <body>\n" +
                "    <div>\n" +
                "      <img src=\"http://www.clipartsfree.net/vector/small/J_Alves_blue_3D_rectangle_Clipart_Free.png\"/>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
        TemplateRendererTestApp generator = new TemplateRendererTestApp(true, false, 3000, false);
        BufferedImage img = generator.generate(template, new Dimension(10, 10), null);

        ImageIO.write(img, "png", new File("tmp/img/test.png"));
    }

    public BufferedImage generate(String html, Dimension size, String baseUrl) throws IOException, SAXException {

        InputStream is = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        URL base = getBaseUrl(baseUrl);
        try(DocumentSource docSource = new StreamDocumentSource(is, base, "text/html;charset=utf-8")) {

            //Parse the input document
            DOMSource parser = new DefaultDOMSource(docSource);
            Document doc = parser.parse();

            //create the media specification
            MediaSpec media = new MediaSpec("screen");
            media.setDimensions(size.width, size.height);
            media.setDeviceDimensions(size.width, size.height);

            //Create the CSS analyzer
            DOMAnalyzer da = new DOMAnalyzer(doc, base);
            da.setMediaSpec(media);
//            da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
            da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
//            da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
//            da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //render form fields using css
            da.getStyleSheets(); //load the author style sheets

            BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, base);
            contentCanvas.setAutoMediaUpdate(false); //we have a correct media specification, do not update
            contentCanvas.getConfig().setClipViewport(cropWindow);
            contentCanvas.getConfig().setLoadImages(loadImages);
            contentCanvas.getConfig().setImageLoadTimeout(timeOut);
            contentCanvas.getConfig().setLoadBackgroundImages(loadBackgroundImages);

            contentCanvas.createLayout(size);
            return contentCanvas.getImage();
        }
    }

    private URL getBaseUrl(String baseUrlPath) throws MalformedURLException {
        return new URL(baseUrlPath == null ? "http://localhost" : baseUrlPath);
    }
}
