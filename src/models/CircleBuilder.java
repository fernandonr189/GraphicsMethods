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
            double newAngle = originalAngle + angle;
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

        buffer = new CustomBuffer((int) floor(highestX), (int) floor(highestY), buffer.getType(), this);

        build(buffer);
        return buffer;
    }
}
