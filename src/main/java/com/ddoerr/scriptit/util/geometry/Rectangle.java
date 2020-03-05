package com.ddoerr.scriptit.util.geometry;

public class Rectangle {
    int x;
    int y;
    int width;
    int height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static Rectangle center(int wholeWidth, int wholeHeight, int width, int height) {
        return new Rectangle((wholeWidth - width) / 2, (wholeHeight - height) / 2, width, height);
    }

    public static Rectangle centerHorizontal(int wholeWidth, int y, int width, int height) {
        return new Rectangle((wholeWidth - width) / 2, y, width, height);
    }

    public int getMinX() {
        return x;
    }

    public int getMaxX() {
        return x + width;
    }

    public int getMinY() {
        return y;
    }

    public int getMaxY() {
        return y + height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(int x, int y) {
        return x >= getMinX() && x <= getMaxX() && y >= getMinY() && y <= getMaxY();
    }

    public boolean contains(double x, double y) {
        return x >= getMinX() && x <= getMaxX() && y >= getMinY() && y <= getMaxY();
    }

    public boolean contains(Rectangle rectangle) {
        return rectangle.getMinX() >= getMinX() && rectangle.getMaxX() <= getMaxX() && rectangle.getMinY() >= getMinY() && rectangle.getMaxY() <= getMaxY();
    }

    public boolean overlaps(Rectangle rectangle) {
        return ( getMaxX() > rectangle.getMinX() && getMinX() < rectangle.getMaxX() ) && ( getMaxY() > rectangle.getMinY() && getMinY() < rectangle.getMaxY() );
    }
}
