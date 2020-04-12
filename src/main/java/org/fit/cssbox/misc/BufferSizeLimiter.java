package org.fit.cssbox.misc;

public class BufferSizeLimiter {
    /**
     * Maximum pixels size for generated image. Full frame 4K screen size in pixels
     */
    private static final int MAX_PIXELS_SIZE = 12_582_912;
    /**
     * Maximum width of result item
     */
    public static final double MAX_WIDTH = 4096.0;
    /**
     * Maximum height of result item
     */
    public static final double MAX_HEIGHT = 3072.0;

    private final int width;
    private final int height;

    public BufferSizeLimiter(int width, int height) {

        if (isOverSized(width, height)) {
            double ratio = Math.min(MAX_WIDTH / width, MAX_HEIGHT / height);
            width = (int) (width * ratio);
            height = (int) (height * ratio);
        }
        this.width = width;
        this.height = height;
    }

    private boolean isOverSized(int width, int height) {
        int pixels = width * height;
        return pixels < 0 || pixels > MAX_PIXELS_SIZE;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
