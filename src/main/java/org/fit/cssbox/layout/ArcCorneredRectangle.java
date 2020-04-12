package org.fit.cssbox.layout;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class ArcCorneredRectangle extends RectangularShape {

    private final Corner topLeft;
    private final Corner topRight;
    private final Corner bottomLeft;
    private final Corner bottomRight;

    double x;
    double y;
    double width;
    double height;

    public ArcCorneredRectangle(double x, double y, double width, double height,
                                BorderRadiusSet borderRadiusSet) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        calcProportions(borderRadiusSet);
        this.topLeft = createCorner(borderRadiusSet.topLeft, CornerAccessor.TOP_LEFT);
        this.topRight = createCorner(borderRadiusSet.topRight, CornerAccessor.TOP_RIGHT);
        this.bottomLeft = createCorner(borderRadiusSet.bottomLeft, CornerAccessor.BOTTOM_LEFT);
        this.bottomRight = createCorner(borderRadiusSet.bottomRight, CornerAccessor.BOTTOM_RIGHT);
    }

    public ArcCorneredRectangle(Rectangle2D rect,
                                BorderRadiusSet borderRadiusSet) {
        this(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), borderRadiusSet);
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public boolean isEmpty() {
        return (width <= 0.0f) || (height <= 0.0f);
    }

    @Override
    public void setFrame(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    public boolean contains(double x, double y) {
        return topLeft.contains(x, y) && topRight.contains(x, y)
                && bottomLeft.contains(x, y) && bottomRight.contains(x, y);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        if (contains(x, y)) {
            return true;
        }
        double y2 = y + h;
        if (contains(x, y2)) {
            return true;
        }
        double x2 = x + w;
        return contains(x2, y) || contains(x2, y2);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new ArcCorneredRectIterator(this, at);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        if (isEmpty() || w <= 0 || h <= 0) {
            return false;
        }
        return (contains(x, y) &&
                contains(x + w, y) &&
                contains(x, y + h) &&
                contains(x + w, y + h));
    }

    enum CornerAccessor {
        TOP_LEFT {
            @Override
            public Corner getCorner(ArcCorneredRectangle rect) {
                return rect.topLeft;
            }

            @Override
            public double getX(RectangularShape rect) {
                return rect.getX();
            }

            @Override
            public double getY(RectangularShape rect) {
                return rect.getY();
            }

            @Override
            public boolean containsInCorner(double x, double y, RectangularShape rect) {
                return x >= rect.getX() && y >= rect.getY();
            }
        },
        BOTTOM_LEFT {
            @Override
            public Corner getCorner(ArcCorneredRectangle rect) {
                return rect.bottomLeft;
            }

            @Override
            public double getX(RectangularShape rect) {
                return rect.getX();
            }

            @Override
            public double getY(RectangularShape rect) {
                return rect.getY() + rect.getHeight();
            }

            @Override
            public boolean containsInCorner(double x, double y, RectangularShape rect) {
                return x >= rect.getX() && y < getY(rect);
            }
        },
        BOTTOM_RIGHT {
            @Override
            public Corner getCorner(ArcCorneredRectangle rect) {
                return rect.bottomRight;
            }

            @Override
            public double getX(RectangularShape rect) {
                return rect.getX() + rect.getWidth();
            }

            @Override
            public double getY(RectangularShape rect) {
                return rect.getY() + rect.getHeight();
            }

            @Override
            public boolean containsInCorner(double x, double y, RectangularShape rect) {
                return x < getX(rect) && y < getY(rect);
            }
        },
        TOP_RIGHT {
            @Override
            public Corner getCorner(ArcCorneredRectangle rect) {
                return rect.topRight;
            }

            @Override
            public double getX(RectangularShape rect) {
                return rect.getX() + rect.getWidth();
            }

            @Override
            public double getY(RectangularShape rect) {
                return rect.getY();
            }

            @Override
            public boolean containsInCorner(double x, double y, RectangularShape rect) {
                return x < getX(rect) && y >= rect.getY();
            }
        };

        public abstract Corner getCorner(ArcCorneredRectangle rect);

        public abstract double getX(RectangularShape rect);

        public abstract double getY(RectangularShape rect);

        public abstract boolean containsInCorner(double x, double y, RectangularShape rect);

        boolean containsInArc(double x, double y, ArcCorneredRectangle rect) {
            // TODO: Optimization. Did it straightforward, did not look at ways to simplify calculations
            double arcCentredX = getArcCentredX(x, rect);
            double arcCentredY = getArcCentredY(y, rect);
            Corner corner = getCorner(rect);
            return (arcCentredX * arcCentredX / (corner.getWidth() * corner.getWidth())) +
                    (arcCentredY * arcCentredY / (corner.getHeight() * corner.getHeight())) <= 1.0;
        }

        private double getArcCentredX(double x, ArcCorneredRectangle rect) {
            return getCorner(rect).getWidth() - Math.abs(x - getX(rect));
        }

        private double getArcCentredY(double y, ArcCorneredRectangle rect) {
            return getCorner(rect).getHeight() - Math.abs(y - getY(rect));
        }
    }

    private class PlainCorner implements Corner {

        private final CornerAccessor accessor;

        public PlainCorner(CornerAccessor accessor) {
            this.accessor = accessor;
        }

        @Override
        public boolean contains(double x, double y) {
            return accessor.containsInCorner(x, y, ArcCorneredRectangle.this);
        }

        @Override
        public double getWidth() {
            return 0.0;
        }

        @Override
        public double getHeight() {
            return 0.0;
        }

        @Override
        public boolean is90Degrees() {
            return true;
        }
    }

    private class ArcCorner implements Corner {
        private final BorderRadiusAngle angle;
        private final CornerAccessor accessor;

        public ArcCorner(BorderRadiusAngle angle, CornerAccessor accessor) {
            this.angle = angle;
            this.accessor = accessor;
        }

        @Override
        public boolean contains(double x, double y) {
            return accessor.containsInCorner(x, y, ArcCorneredRectangle.this) &&
                    ((x - getX() > angle.horizontal) || (y - getY() > angle.vertical) ||
                            accessor.containsInArc(x, y, ArcCorneredRectangle.this));
        }

        @Override
        public double getWidth() {
            return angle.horizontal;
        }

        @Override
        public double getHeight() {
            return angle.vertical;
        }

        @Override
        public boolean is90Degrees() {
            return false;
        }
    }

    private void calcProportions(BorderRadiusSet borderRadiusSet) {
        if (width > 0.0) {
            calcProportionsHorizontal(borderRadiusSet.topLeft, borderRadiusSet.topRight);
            calcProportionsHorizontal(borderRadiusSet.bottomLeft, borderRadiusSet.bottomRight);
        }
        if (height > 0.0) {
            calcProportionsVertical(borderRadiusSet.topLeft, borderRadiusSet.bottomLeft);
            calcProportionsVertical(borderRadiusSet.topRight, borderRadiusSet.bottomRight);
        }
    }

    private void calcProportionsHorizontal(BorderRadiusAngle angle1, BorderRadiusAngle angle2) {
        if (angle1 == null) {
            calcProportionsHorizontal(angle2);
        } else if (angle2 == null) {
            calcProportionsHorizontal(angle1);
        } else {
            double angleSum = angle1.horizontal + angle2.horizontal;
            if (angleSum > width) {
                angle1.horizontal = angle1.horizontal * width / angleSum;
                angle2.horizontal = angle2.horizontal * width / angleSum;
            }
        }
    }

    private void calcProportionsHorizontal(BorderRadiusAngle angle) {
        if (angle != null) {
            if(angle.horizontal > width) {
                angle.horizontal = width;
            }
        }
    }

    private void calcProportionsVertical(BorderRadiusAngle angle1, BorderRadiusAngle angle2) {
        if (angle1 == null) {
            calcProportionsVertical(angle2);
        } else if (angle2 == null) {
            calcProportionsVertical(angle1);
        } else {
            double angleSum = angle1.vertical + angle2.vertical;
            if (angleSum > height) {
                angle1.vertical = angle1.vertical * height / angleSum;
                angle2.vertical = angle2.vertical * height / angleSum;
            }
        }
    }

    private void calcProportionsVertical(BorderRadiusAngle angle) {
        if (angle != null) {
            if(angle.vertical > height) {
                angle.vertical = height;
            }
        }
    }

    private Corner createCorner(BorderRadiusAngle angle, CornerAccessor accessor) {
        if (angle == null) {
            return new PlainCorner(accessor);
        }
        return new ArcCorner(angle, accessor);
    }
}