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

        buffer.BresenhamLine(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), Color.red);
        buffer.BresenhamLine(points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY(), Color.red);
        buffer.BresenhamLine(points[2].getX(),points[2].getY(), points[3].getX(), points[3].getY(), Color.red);
        buffer.BresenhamLine(points[3].getX(),points[3].getY(), points[0].getX(), points[0].getX(), Color.red);
        buffer.floodFill(points[4].getX(), points[4].getY(), Color.red);
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
        
        build(buffer);
        return buffer;
    }
}
