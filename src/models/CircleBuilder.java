package models;

import java.awt.*;

import static java.lang.Math.*;

public class CircleBuilder implements BuildMethods{

    private Point p1;
    private Point p2;
    private Point p3;
    private Point p4;
    private Point p5;
    private int radius;
    private boolean isInitialized = false;

    @Override
    public void build(CustomBuffer buffer) {
        if(!isInitialized) {
            p1 = new Point(100, 100);
            p2 = new Point(20, 20);
            p3 = new Point(180, 180);
            p4 = new Point(180, 20);
            p5 = new Point(20, 180);
            radius = 80;
            isInitialized = true;
        }

        buffer.basicCircle(p1.getX(), p1.getY(),radius, Color.blue);
        buffer.floodFill(p1.getX(), p1.getY(), Color.blue);
        buffer.DDALine(p2.getX(), p2.getY(), p3.getX(), p3.getY(), Color.blue);
        //buffer.DDALine(p4.getX(), p4.getY(), p5.getX(), p5.getY(), Color.blue);
    }

    @Override
    public CustomBuffer scale(CustomBuffer buffer, double factor) {
        int newWidth = (int) floor(buffer.getWidth() * factor);
        int newHeight = (int) floor(buffer.getHeight() * factor);

        int[] center = {newWidth / 2, newHeight / 2};
        p1 = new Point(center[0], center[1]);
        p2 = getNewPoint(p1, p2, factor);
        p3 = getNewPoint(p1, p3, factor);
        //p4 = getNewPoint(p1, p4, factor);
        //p5 = getNewPoint(p1, p5, factor);

        CustomBuffer newBuffer = new CustomBuffer(newWidth, newHeight, buffer.getType(), this);

        radius = (int) floor(radius * factor);
        //newBuffer.basicCircle(p1.getX(), p1.getY(), (int) floor(radius * factor), Color.blue);
        //newBuffer.floodFill(p1.getX(), p1.getY(), Color.blue);
        newBuffer.DDALine(p2.getX(), p2.getY(), p3.getX(), p3.getY(), Color.blue);
        //newBuffer.DDALine(p4.getX(), p4.getY(), p5.getX(), p5.getY(), Color.blue);

        buffer = newBuffer;
        return buffer;
    }

    private Point getNewPoint(Point origin, Point target, double scale) {
        int dx = target.getX() - origin.getX();
        int dy = target.getY() - origin.getY();

        int newDx = (int) floor(dx * scale);
        int newDy = (int) floor(dy * scale);

        return new Point(origin.getX() + newDx, origin.getY() + newDy);
    }
}
