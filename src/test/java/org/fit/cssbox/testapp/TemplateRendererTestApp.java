package org.fit.cssbox.testapp;

import cz.vutbr.web.css.MediaSpec;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;
import org.fit.cssbox.layout.ArcCorneredRectangle;
import org.fit.cssbox.layout.BorderRadiusAngle;
import org.fit.cssbox.layout.BorderRadiusSet;
import org.fit.cssbox.layout.BrowserCanvas;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
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


    private void init(boolean loadImages, boolean loadBackgroundImages, int timeOut, boolean cropWindow) {
        this.cropWindow = cropWindow;
        this.loadImages = loadImages;
        this.loadBackgroundImages = loadBackgroundImages;
        this.timeOut = timeOut;
    }

    @Ignore
    @Test
    public void shapeOrder() throws IOException {
        int width = 500;
        int height = 500;
        Shape rect = new ArcCorneredRectangle(0, 0, width, height,
//                new BorderRadiusSet(new BorderRadiusAngle(250, 250), new BorderRadiusAngle(100, 150),
//                        null, new BorderRadiusAngle(80, 60)));

        new BorderRadiusSet(new BorderRadiusAngle(500, 500), new BorderRadiusAngle(500, 500),
                new BorderRadiusAngle(500, 500), new BorderRadiusAngle(500, 500)));
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillShape(rect,img);
        ImageIO.write(img, "png", new File("tmp/img/shape.png"));
//        clipShape(rect, img);
//        ImageIO.write(img, "png", new File("tmp/img/shape2.png"));
//        clipImage(rect, img);
//        ImageIO.write(img, "png", new File("tmp/img/shape3.png"));
        textureShape(rect, img);
        ImageIO.write(img, "png", new File("tmp/img/shape4.png"));
    }

    private void textureShape(Shape rect, BufferedImage img) throws IOException {
        BufferedImage stars = ImageIO.read(TemplateRendererTestApp.class.getResourceAsStream("/stars.jpg"));
        Graphics2D g = initGraphics(img);
        clear(g);
        TexturePaint paint = new TexturePaint(stars, rect.getBounds2D());
        g.setPaint(paint);
        g.fill(rect);
        g.dispose();
    }

    private void clipImage(Shape rect, BufferedImage img) throws IOException {
        BufferedImage stars = ImageIO.read(TemplateRendererTestApp.class.getResourceAsStream("/stars.jpg"));
        Graphics2D g = initGraphics(img);
        clear(g);
        g.setClip(rect);
        g.drawImage(stars, null, null);
        g.dispose();
    }

    private void clipShape(Shape rect, BufferedImage img) {
        Graphics2D g = initGraphics(img);
        clear(g);
        g.setColor(Color.GREEN);
        g.setClip(rect);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.dispose();
    }

    private static void clear(Graphics2D g) {
        g.setBackground(new Color(255, 255, 255, 0));
    }

    private static void fillShape(Shape rect, BufferedImage img) {
        Graphics2D g = initGraphics(img);
        g.setColor(Color.ORANGE);
        g.fill(rect);
        g.dispose();
    }

    private static Graphics2D initGraphics(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return g;
    }

    @Ignore
    @Test
    public void drawBorder() throws IOException, SAXException {
        String template = "<!DOCTYPE html>"
                          + "<html>"
                          + "  <head> "
                          + "    <meta charset=\"UTF-8\">"
                          + "    <style type=\"text/css\">"
                          + "       .bordered {"
//                          + "           border-radius: 35px 10px 60px 5px;"
                          + "           border: 2px solid #73AD21;"
                          + "           padding: 20px;"
                          + "           width: 100px; "
                          + "           height: 100px;"
//                          + "           background-color: red;"
                          + "           overflow: hidden;"
                          + "           background-image: url(http://pngimg.com/uploads/scratches/scratches_PNG6173.png);"
                          + "       }"
                          + "    </style>"
                          + "  </head>"
                          + "  <body>"
                          + "    <div class=\"bordered\">"
//                          + "      <img src=\"https://static.fjcdn.com/large/pictures/e7/1c/e71c16_5559546.jpg\"/>"
                          + "    </div>"
                          + "  </body>"
                          + "</html>";

        run(template, "tmp/img/border.png");
    }

    @Ignore
    @Test
    public void drawImage() throws IOException, SAXException {
        String template = "<!DOCTYPE html>" +
                "<html lang=\"en\">"
                          + "  <head> "
                          + "    <meta charset=\"UTF-8\">"
                          + "    <style type=\"text/css\">"
                          + " body { margin:0; padding: 0;} "
                          + "    </style>"
                          + "  </head>" +
                "  <body>" +
                "    <div>" +
                "      <img src=\"https://static.fjcdn.com/large/pictures/e7/1c/e71c16_5559546.jpg\"/>" +
                "    </div>" +
                "  </body>" +
                "</html>";

        run(template, "tmp/img/test.png");
    }

    private void run(String template, String path) throws IOException, SAXException {
        init(true, true, 3000, false);
        BufferedImage img = generate(template, new Dimension(10, 10), null);

        ImageIO.write(img, "png", new File(path));
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
