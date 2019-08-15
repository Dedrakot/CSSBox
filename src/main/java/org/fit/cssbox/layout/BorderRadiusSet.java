package org.fit.cssbox.layout;

public class BorderRadiusSet {
    public final BorderRadiusAngle topLeft;
    public final BorderRadiusAngle topRight;
    public final BorderRadiusAngle bottomLeft;
    public final BorderRadiusAngle bottomRight;

    public BorderRadiusSet(BorderRadiusAngle topLeft, BorderRadiusAngle topRight, BorderRadiusAngle bottomLeft, BorderRadiusAngle bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }
}
