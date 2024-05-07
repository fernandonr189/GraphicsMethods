package models;

import java.awt.*;

import static java.lang.Math.*;

public class SquareBuilder implements BuildMethods{

    private Point[] points;
    private boolean isInitialized = false;

    @Override
    public void build(CustomBuffer buffer) {
        if(!isInitialized) {
            points = new Point[5];
            points[0] = new Point(20, 20);
            points[1] = new Point(180, 20);
            points[2] = new Point(180, 180);
            points[3] = new Point(20, 280);
            points[4] = new Point(21, 21);
            isInitialized = true;
        }

        buffer.BresenhamLine(
                (int) floor(points[0].getX()),
                (int) floor(points[0].getY()),
                (int) floor(points[1].getX()),
                (int) floor(points[1].getY()),
                Color.red);
        buffer.BresenhamLine(
                (int) floor(points[1].getX()),
                (int) floor(points[1].getY()),
                (int) floor(points[2].getX()),
                (int) floor(points[2].getY()),
                Color.red);
        buffer.BresenhamLine(
                (int) floor(points[2].getX()),
                (int) floor(points[2].getY()),
                (int) floor(points[3].getX()),
                (int) floor(points[3].getY()),
                Color.red);
        buffer.BresenhamLine(
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
        int newWidth = (int) floor(buffer.getWidth() * factor);
        int newHeight = (int) floor(buffer.getHeight() * factor);
        CustomBuffer newBuffer = new CustomBuffer(newWidth, newHeight, buffer.getType(), this);
        buffer = newBuffer;
        
        for(int i = 0; i < points.length; i++) {
            points[i] = new Point(
                points[i].getX() * factor,
                points[i].getY() * factor);
        }
        
        build(buffer);
        return buffer;
    }
}
