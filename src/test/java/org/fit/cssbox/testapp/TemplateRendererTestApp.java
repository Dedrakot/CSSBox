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
    public void drawBorderedBackgroundImageCircle() throws IOException, SAXException {
        String template = "<!DOCTYPE html>"
                          + "<html>"
                          + "  <head> "
                          + "    <meta charset=\"UTF-8\">"
                          + "    <style type=\"text/css\">"
                          + "       .bordered {"
                          + "           border-radius:100px;"
                          + "           background-color: red;"
                          + "           overflow: hidden;"
                          + "       };"
                          + "    </style>"
                          + "  </head>"
                          + "  <body>"
                          + "    <div class=\"bordered\">"
                          + "  <img src=\"https://ladyelena.ru/wp-content/uploads/2014/01/k-chemu-snitsya-mysh-malenkaya-2.jpg\"/>"
                          + "    </div>"
                          + "  </body>"
                          + "</html>";
        run(template, "tmp/img/borderedBackgroundImageCircle.png");
    }
    @Ignore
    @Test
    public void drawBorderedCircle() throws IOException, SAXException {
        String template = "<!DOCTYPE html>"
                          + "<html>"
                          + "  <head> "
                          + "    <meta charset=\"UTF-8\">"
                          + "    <style type=\"text/css\">"
                          + "       .bordered {"
                          + "           border-radius:200px;"
                          + "           border: 1px solid red;"
                          + "           width: 500px;"
                          + "           height: 500px;"
                          + "           background-color: black;"
                          + "           background-image: url(https://ladyelena.ru/wp-content/uploads/2014/01/k-chemu-snitsya-mysh-malenkaya-2.jpg);"
                          + "           overflow: visible;"
                          + "       }"
                          + "       .bordered1 {"
                          + "           border-radius:200px;"
                          + "           border: 2px solid red;"
                          + "           width: 500px;"
                          + "           height: 500px;"
                          + "           background-color: black;"
                          + "           overflow: visible;"
                          + "           margin-top: 5px;"
                          + "       }"
                          + "       .bordered2 {"
                          + "           border-radius:200px;"
                          + "           border: 30px solid red;"
                          + "           width: 500px;"
                          + "           height: 500px;"
                          + "           background-color: black;"
                          + "           overflow: visible;"
                          + "           margin-top: 5px;"
                          + "       };"

                          + "    </style>"
                          + "  </head>"
                          + "  <body>"
                          + "    <div class=\"bordered\">"
                          + "    </div>"
                          + "    <div class=\"bordered1\">"
                          + "    </div>"
                          + "    <div class=\"bordered2\">"
                          + "    </div>"
                          + "  </body>"
                          + "</html>";
        run(template, "tmp/img/borderedCircle.png");
    }


    @Ignore
    @Test
    public void drawBorderedFigure() throws IOException, SAXException {
        String template = "<!DOCTYPE html>"
                          + "<html>"
                          + "  <head> "
                          + "    <meta charset=\"UTF-8\">"
                          + "    <style type=\"text/css\">"
                          + "       .bordered {"
                          + "           border-radius:45px;"
                          + "           border: 15px dashed red;"
                          + "           margin-top: 37px;"
                          + "           width: 120px;"
                          + "           height: 120px;"
                          + "           background-color: white;"
                          + "           overflow: hidden;"
                          + "       }.bordered0 {"
                          + "           border-radius: 0px 185px 422px 40px;"
                          + "           border: 1px dashed red;"
                          + "           width: 500px;"
                          + "           height: 500px;"
                          + "           background-color: black;"
                          + "           overflow: visible;"
                          + "       }"
                          + "       .bordered2 {"
                          + "           padding-left: 50px;"
                          + "           border-radius: 110px;"
                          + "           border: 11px dashed blue;"
                          + "           width: 90px;"
                          + "           height: 120px;"
                          + "           background-color: lightgreen;"
                          + "           overflow: hidden;"
                          + "       }"
                          + "       .bordered3 {"
                          + "           padding-left: 50px;"
                          + "           border-radius: 110px;"
                          + "           border: 11px dashed #FF0055;"
                          + "           width: 90px;"
                          + "           height: 120px;"
                          + "           background-color: white;"
                          + "           overflow: hidden;"
                          + "       };"
                          + "    </style>"
                          + "  </head>"
                          + "  <body>"
                          + "    <div class=\"bordered0\">"
                          + "    <div class=\"bordered2\">"
                          + "    <div class=\"bordered\">"
                          + "    </div>"
                          + "    </div>"
                          + "    <div class=\"bordered3\">"
                          + "    </div>"
                          + "    </div>"
                          + "  </body>"
                          + "</html>";
        run(template, "tmp/img/borderedFigure.png");
    }

    @Ignore
    @Test
    public void drawBorderedInlineBlocks() throws IOException, SAXException {
        String template = "<!DOCTYPE html>"
                          + "<html>"
                          + "  <head> "
                          + "    <meta charset=\"UTF-8\">"
                          + "    <style type=\"text/css\">"
                          + "       .bordered {"
                          + "           border-radius: 50px;"
                          + "           border: 39px dashed #00FF00;"
                          + "           width: 500px;"
                          + "           height: 500px;"
                          + "        background-color: red;"
                          + "       overflow: hidden"
                          + "       }"
                          + ".bordered li {"
                          + "  display: inline-block;"
                          + "  border-radius: 15px;"
                          + "  border: 10px solid #00FFFF;"
                          + "  font-size: 20px;"
                          + "  padding: 5px;"
                          + "};"
                          + "    </style>"
                          + "  </head>"
                          + "  <body>"
                          + "    <div class=\"bordered\">"
                          + "  <img src=\"https://ladyelena.ru/wp-content/uploads/2014/01/k-chemu-snitsya-mysh-malenkaya-2.jpg\"/>"
                          + "  <li><a href=\"#home\">Home</a></li>"
                          + "  <li><a href=\"#about\">About Us</a></li>"
                          + "  <li><a href=\"#clients\">Our Clients</a></li>"
                          + "  <li><a href=\"#contact\">Contact Us</a></li>"
                          + "    </div>"
                          + "  </body>"
                          + "</html>";
        run(template, "tmp/img/borderedInlineBlocks.png");
    }

    @Ignore
    @Test
    public void drawBorder() throws IOException, SAXException {
        String template = "<!DOCTYPE html>"
                          + "<html>"
                          + "  <head> "
                          + "    <meta charset=\"UTF-8\">"
                          + "    <style type=\"text/css\">"
                          + " .bordered {"
                          + "           border-radius: 600px 600px 600px 600px;"
                          + "           border: 20px dashed #73AD21;"
                          + "           width: 1200px;"
                          + "           height: 1200px;"
                          + "           background-size: 100px 100px;"
                          + "           background-image: url(https://ladyelena.ru/wp-content/uploads/2014/01/k-chemu-snitsya-mysh-malenkaya-2.jpg);"
                          + "        overflow: hidden;"
                          + "       } "
                          + " .bordered2 {"
                          + "        border-radius: 99px 322px 60px 33px;"
                          + "        border-width: 3px;"
                          + "        border-color: #FFAD21;"
                          + "        border-style: solid;"
                          + "        margin-left: 350px;"
                          + "        width: 850px;"
                          + "        height: 700px;"
                          + "        overflow: hidden;"
                          + "        background-image: url(http://nerabotaetsite.ru/wp-content/uploads/2016/05/yandex-kartinki.jpg);"
                          + "      }"
                          + " .bordered3 {"
                          + "        border-radius: 99px;"
                          + "        border: 10px;"
                          + "        margin-left: 200px;"
                          + "        width: 700px;"
                          + "        height: 200px;"
                          + "        overflow: hidden;"
                          + "        background-color: red;"
                          + "      };"
                          + "    </style>"
                          + "  </head>"
                          + "  <body>"
                          + "    <div class=\"bordered\">"
                          + "    <div style=\"font-size: 70pt; color: red; text-decoration: underline;\">"
                          + "       What was going on here"
                          + "    </div>"
                          + "    <div class=\"bordered2\">"
                          + "       Lorem Ipsum is simply dummy text of the printing and typesetting industry. "
                          + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s  "
                          + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s  "
                          + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s  "
                          + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s  "
                          + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s  "
                          + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s  "
                          + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s  "
                          + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s  "

                          + "    <div class=\"bordered3\">"
                          + "               Contrary to popular belief, Lorem Ipsum is not simply random text. "
                          + "    </div>"
                          + "    </div>"
                          + "  <img src=\"https://m-ua.info/wp-content/uploads/2017/12/color.jpg\"/>"
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
            da.addStyleSheet(null, "body {position:absolute;}", DOMAnalyzer.Origin.AGENT);
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
