package models;

import java.awt.*;

import static java.lang.Math.*;

public class CircleBuilder implements BuildMethods{

    private Point[] points;
    private double radius;
    private boolean isInitialized = false;
    private double height;
    private double width;

    @Override
    public void build(CustomBuffer buffer) {
        if(!isInitialized) {
            height = buffer.getHeight();
            width = buffer.getWidth();
            points = new Point[5];
            points[0] = new Point(100, 100);
            points[1] = new Point(20, 20);
            points[2] = new Point(180, 180);
            points[3] = new Point(180, 20);
            points[4] = new Point(20, 180);
            radius = 80;
            isInitialized = true;
        }

        buffer.basicCircle(
                (int) floor(points[0].getX()),
                (int) floor(points[0].getY()),
                (int) floor(radius), Color.blue);

        buffer.floodFill(
                (int) floor(points[0].getX()),
                (int) floor(points[0].getY()),
                Color.blue);

        buffer.DDALine(
                (int) floor(points[1].getX()),
                (int) floor(points[1].getY()),
                (int) floor(points[2].getX()),
                (int) floor(points[2].getY()),
                Color.blue);

        buffer.DDALine(
                (int) floor(points[3].getX()),
                (int) floor(points[3].getY()),
                (int) floor(points[4].getX()),
                (int) floor(points[4].getY()),
                Color.blue);
    }

    @Override
    public CustomBuffer scale(CustomBuffer buffer, double factor) {
        width = width * factor;
        height = height * factor;
        buffer = new CustomBuffer((int) floor(width), (int) floor(height), buffer.getType(), this);
        
        for(int i = 0; i < points.length; i++) {
            points[i] = new Point(
                points[i].getX() * factor,
                points[i].getY() * factor);
        }

        radius = radius * factor;

        build(buffer);
        return buffer;
    }
}
