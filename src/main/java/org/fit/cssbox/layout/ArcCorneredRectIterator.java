package org.fit.cssbox.layout;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

import org.fit.cssbox.layout.ArcCorneredRectangle.CornerAccessor;

class ArcCorneredRectIterator implements PathIterator {

    private ArcCorneredRectangle rect;
    private AffineTransform affine;
    private int index;
    private int cornerNum;

    ArcCorneredRectIterator(ArcCorneredRectangle rect, AffineTransform affine) {
        this.rect = rect;
        this.affine = affine;
    }

    @Override
    public int getWindingRule() {
        return WIND_NON_ZERO;
    }

    @Override
    public boolean isDone() {
        return index >= ctrlpts.length;
    }

    @Override
    public void next() {
        index++;
    }

    @Override
    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("roundrect iterator out of bounds");
        }
        if (cornerNum > 3) {
            cornerNum = 0;
        }
        CornerAccessor accessor = ArcCorneredRectangle.CornerAccessor.values()[cornerNum];
        Corner corner = accessor.getCorner(rect);
        if (corner.is90Degrees()) {
            coords[0] = (float) accessor.getX(rect);
            coords[1] = (float) accessor.getY(rect);
            if (affine != null) {
                affine.transform(coords, 0, coords, 0, 1);
            }
            cornerNum++;
            int type = TYPES[index];
            if (index + 1 < TYPES.length && TYPES[index + 1] == SEG_CUBICTO) {
                index++;
            }
            return type;
        } else {
            double[] ctrls = ctrlpts[index];
            int nc = 0;
            for (int i = 0; i < ctrls.length; i += 4) {
                coords[nc++] = (float) (rect.x + ctrls[i] * rect.width + ctrls[i + 1] * corner.getWidth());
                coords[nc++] = (float) (rect.y + ctrls[i + 2] * rect.height + ctrls[i + 3] * corner.getHeight());
            }
            if (affine != null) {
                affine.transform(coords, 0, coords, 0, nc / 2);
            }
            int type = TYPES[index];
            if (type != SEG_LINETO) {
                cornerNum++;
            }
            return type;
        }
    }

    @Override
    public int currentSegment(double[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("roundrect iterator out of bounds");
        }
        CornerAccessor accessor = ArcCorneredRectangle.CornerAccessor.values()[cornerNum];
        Corner corner = accessor.getCorner(rect);
        if (corner.is90Degrees()) {
            coords[0] = accessor.getX(rect);
            coords[1] = accessor.getY(rect);
            if (affine != null) {
                affine.transform(coords, 0, coords, 0, 1);
            }
            cornerNum++;
            int type = TYPES[index];
            if (index + 1 < TYPES.length && TYPES[index + 1] == SEG_CUBICTO) {
                index++;
            }
            return type;
        } else {
            double[] ctrls = ctrlpts[index];
            int nc = 0;
            for (int i = 0; i < ctrls.length; i += 4) {
                coords[nc++] = (rect.x + ctrls[i] * rect.width + ctrls[i + 1] * corner.getWidth());
                coords[nc++] = (rect.y + ctrls[i + 2] * rect.height + ctrls[i + 3] * corner.getHeight());
            }
            if (affine != null) {
                affine.transform(coords, 0, coords, 0, nc / 2);
            }
            int type = TYPES[index];
            if (type != SEG_LINETO) {
                cornerNum++;
            }
            return type;
        }
    }

    private static final double ANGLE = Math.PI / 20;
    private static final double A = 1.0 - Math.cos(ANGLE);
    private static final double B = Math.tan(ANGLE);
    private static final double C = Math.sqrt(1.0 + B * B) - 1 + A;
    private static final double CV = 4.0 / 3.0 * A * B / C;
    private static final double ACV = (1.0 - CV) / 2.0;

    private static final double ctrlpts[][] = {
            {0.0, 0.0, 0.0, 1.0},
            {0.0, 0.0, 1.0, -1.0},
            {0.0, 0.0, 1.0, -ACV,
             0.0, ACV, 1.0, 0.0,
             0.0, 1.0, 1.0, 0.0},
            {1.0, -1.0, 1.0, 0.0},
            {1.0, -ACV, 1.0, 0.0,
             1.0, 0.0, 1.0, -ACV,
             1.0, 0.0, 1.0, -1.0},
            {1.0, 0.0, 0.0, 1.0},
            {1.0, 0.0, 0.0, ACV,
             1.0, -ACV, 0.0, 0.0,
             1.0, -1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0, 0.0},
            {0.0, ACV, 0.0, 0.0,
             0.0, 0.0, 0.0, ACV,
             0.0, 0.0, 0.0, 1.0},
            {},
            };
    private static final int TYPES[] = {
            SEG_MOVETO,
            SEG_LINETO, SEG_CUBICTO,
            SEG_LINETO, SEG_CUBICTO,
            SEG_LINETO, SEG_CUBICTO,
            SEG_LINETO, SEG_CUBICTO,
            SEG_CLOSE,
            };

}
