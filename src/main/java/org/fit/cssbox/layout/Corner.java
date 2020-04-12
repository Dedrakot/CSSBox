package org.fit.cssbox.layout;

public interface Corner {
    boolean contains(double x, double y);
    double getWidth();
    double getHeight();
    boolean is90Degrees();
}