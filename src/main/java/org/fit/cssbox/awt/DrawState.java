package org.fit.cssbox.awt;

import cz.vutbr.web.css.CSSProperty;
import org.fit.cssbox.layout.ArcCorneredRectangle;
import org.fit.cssbox.layout.BlockBox;
import org.fit.cssbox.layout.ElementBox;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class DrawState {
    private final AffineTransform transform;
    private final Shape clip;
    private final boolean restoreClip;

    public DrawState(AffineTransform transform, Shape clip, boolean restoreClip) {
        this.transform = transform;
        this.clip = clip;
        this.restoreClip = restoreClip;
    }

    private AffineTransform getTransform() {
        return transform;
    }

    private Shape getClip() {
        return clip;
    }

    public void restore(GraphicsRenderer graphicsRenderer, ElementBox elem) {
        Graphics2D g = graphicsRenderer.g;
        if (restoreClip) {
            g.setClip(getClip());
        }

        if(elem.getBorderRadiusSet()!=null) {
            BlockBox blockBox = (BlockBox) elem;
            if (CSSProperty.Overflow.HIDDEN.equals(blockBox.getOverflowX()) || CSSProperty.Overflow.HIDDEN.equals(blockBox.getOverflowY())) {
                Rectangle2D.Float base = GraphicsRenderer.getBorderRadiusBounds(elem);
                graphicsRenderer.drawBorderRadiusBorder(elem, g, new ArcCorneredRectangle(base, elem.getBorderRadiusSet()));
            }
        }

        //restore the stransformations
        AffineTransform origAt = getTransform();
        if (origAt != null) {
            g.setTransform(origAt);
        }
    }
}
