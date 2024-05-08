package models;

import java.awt.*;

import static java.lang.Math.*;

public class SquareBuilder implements BuildMethods{

    private Point[] points;
    private boolean isInitialized = false;
    private double height;
    private double width;

    @Override
    public void build(CustomBuffer buffer) {
        if(!isInitialized) {
            height = buffer.getHeight();
            width = buffer.getWidth();
            points = new Point[5];
            points[0] = new Point(20, 20);
            points[1] = new Point(180, 20);
            points[2] = new Point(180, 180);
            points[3] = new Point(20, 180);
            points[4] = new Point(100, 100);
            isInitialized = true;
        }

        buffer.DDALine(
                (int) floor(points[0].getX()),
                (int) floor(points[0].getY()),
                (int) floor(points[1].getX()),
                (int) floor(points[1].getY()),
                Color.red);
        buffer.DDALine(
                (int) floor(points[1].getX()),
                (int) floor(points[1].getY()),
                (int) floor(points[2].getX()),
                (int) floor(points[2].getY()),
                Color.red);
        buffer.DDALine(
                (int) floor(points[2].getX()),
                (int) floor(points[2].getY()),
                (int) floor(points[3].getX()),
                (int) floor(points[3].getY()),
                Color.red);
        buffer.DDALine(
                (int) floor(points[3].getX()),
                (int) floor(points[3].getY()),
                (int) floor(points[0].getX()),
                (int) floor(points[0].getY()),
                Color.red);
        buffer.floodFill(
                (int) floor(points[4].getX()),
                (int) floor(points[4].getY()),
                Color.red);
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

        build(buffer);
        return buffer;
    }

    @Override
    public CustomBuffer rotate(CustomBuffer buffer, double angle) { 
        double[] center = {
            (double) buffer.getWidth() / 2,
            (double) buffer.getHeight() / 2
        };

        double highestX = buffer.getWidth();
        double highestY = buffer.getHeight();

        for(int i = 0; i < points.length; i++) {
            double dx = points[i].getX() - center[0];
            double dy = points[i].getY() - center[1];
            double b = sqrt(pow(dx, 2) + pow(dy, 2));
            double originalAngle = atan2(dy, dx);
            double newAngle = originalAngle - angle;
            double newX = b * cos(newAngle) + center[0];
            double newY = b * sin(newAngle) + center[1];

            if(newX > highestX) {
                highestX = newX;
            }

            if(newY > highestY) {
                highestY = newY;
            }

            points[i] = new Point(newX, newY);
        }

        buffer = new CustomBuffer((int) floor(highestX) + 5, (int) floor(highestY) + 5, buffer.getType(), this);

        build(buffer);
        return buffer;
    }
}
