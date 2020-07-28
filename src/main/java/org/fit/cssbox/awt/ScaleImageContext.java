package org.fit.cssbox.awt;

import org.fit.cssbox.layout.Rectangle;
import org.fit.cssbox.layout.ReplacedImage;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public interface ScaleImageContext {
    default void drawScaledImage(Graphics2D g, BufferedImage image,
                                 float x, float y, float w, float h,
                                 float origw, float origh, ImageObserver observer) {
        g.drawImage(image,
                Math.round(x), Math.round(y), Math.round(x + w), Math.round(y + h),
                0, 0, Math.round(origw), Math.round(origh),
                observer);
    }

    // draw image of the given size and position
    default void drawImageInBounds(ReplacedImage img, Graphics2D g, Rectangle bounds) {
        final AffineTransform tr = new AffineTransform();
        tr.translate(bounds.x, bounds.y);
        tr.scale(bounds.width / img.getIntrinsicWidth(), bounds.height / img.getIntrinsicHeight());
        g.drawImage(((BitmapImage) img.getImage()).getBufferedImage(), tr, null);
    }
}
