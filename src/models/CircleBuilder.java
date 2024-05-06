package models;

import java.awt.*;

import static java.lang.Math.*;

public class CircleBuilder implements BuildMethods{

    private Point[] points;
    private int radius;
    private boolean isInitialized = false;

    @Override
    public void build(CustomBuffer buffer) {
        if(!isInitialized) {
            points = new Point[5];
            points[0] = new Point(100, 100);
            points[1] = new Point(20, 20);
            points[2] = new Point(180, 180);
            points[3] = new Point(180, 20);
            points[4] = new Point(20, 180);
            radius = 80;
            isInitialized = true;
        }

        buffer.basicCircle(points[0].getX(), points[0].getY(), radius, Color.blue);
        buffer.floodFill(points[0].getX(), points[0].getY(), Color.blue);
        buffer.DDALine(points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY(), Color.blue);
        buffer.DDALine(points[3].getX(), points[3].getY(), points[4].getX(), points[4].getY(), Color.blue);
    }

    @Override
    public CustomBuffer scale(CustomBuffer buffer, double factor) {
        int newWidth = (int) floor(buffer.getWidth() * factor);
        int newHeight = (int) floor(buffer.getHeight() * factor);
        CustomBuffer newBuffer = new CustomBuffer(newWidth, newHeight, buffer.getType(), this);
        buffer = newBuffer;
        
        for(int i = 0; i < points.length; i++) {
            points[i] = new Point(
                (int) floor(points[i].getX() * factor),
                (int) floor(points[i].getY() * factor));
        }

        radius = (int) floor(radius * factor);

        build(buffer);
        return buffer;
    }
}
