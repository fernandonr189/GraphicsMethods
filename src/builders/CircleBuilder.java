package builders;

import models.BuildMethods;
import models.CustomBuffer;
import models.Point;

import java.awt.*;

import static java.lang.Math.*;

public class CircleBuilder implements BuildMethods {

    private models.Point[] points;
    private models.Point[] originalPoints;
    private double radius;
    private double originalRadius;
    private boolean isInitialized = false;
    private double height;
    private double width;

    public int rebuildCount = 0;

    @Override
    public void build(CustomBuffer buffer) {
        rebuildCount++;
        if(!isInitialized) {
            height = buffer.getHeight();
            width = buffer.getWidth();
            points = new models.Point[5];
            points[0] = new models.Point(100, 100);
            points[1] = new models.Point(20, 20);
            points[2] = new models.Point(180, 180);
            points[3] = new models.Point(180, 20);
            points[4] = new models.Point(20, 180);
            originalPoints = points.clone();
            radius = 80;
            originalRadius = radius;
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

        buffer.BresenhamLine(
                (int) floor(points[1].getX()),
                (int) floor(points[1].getY()),
                (int) floor(points[2].getX()),
                (int) floor(points[2].getY()),
                Color.blue);

        buffer.BresenhamLine(
                (int) floor(points[3].getX()),
                (int) floor(points[3].getY()),
                (int) floor(points[4].getX()),
                (int) floor(points[4].getY()),
                Color.blue);
    }

    @Override
    public CustomBuffer scale(CustomBuffer buffer, double factor, boolean rebuild) {
        width = width * factor;
        height = height * factor;

        double lowestX = buffer.getWidth();
        double lowestY = buffer.getHeight();
        double highestX = 0;
        double highestY = 0;
        
        for(int i = 0; i < points.length; i++) {
            points[i] = new models.Point(
                originalPoints[i].getX() * factor,
                originalPoints[i].getY() * factor);

            if(points[i].getX() > highestX) {
                highestX = points[i].getX();
            }
            if(points[i].getY() > highestY) {
                highestY = points[i].getY();
            }
            if(points[i].getX() < lowestX) {
                lowestX = points[i].getX();
            }
            if(points[i].getY() < lowestY) {
                lowestY = points[i].getY();
            }
        }

        for(int i = 0; i < points.length; i++) {
            double newX = points[i].getX() - lowestX + 20;
            double newY = points[i].getY() - lowestY + 20;
            points[i] = new Point(newX, newY);
        }

        int bufferWidth = (int) floor(highestX - lowestX) + 40;
        int bufferHeight = (int) floor(highestY - lowestY) + 40;

        radius = originalRadius * factor;

        buffer = new CustomBuffer(bufferWidth, bufferHeight, buffer.getType(), this);

        build(buffer);
        return buffer;
    }


    @Override
    public CustomBuffer rotate(CustomBuffer buffer, double angle, boolean rebuild) {
        double[] center = {
                (double) buffer.getWidth() / 2,
                (double) buffer.getHeight() / 2
        };

        double lowestX = buffer.getWidth();
        double lowestY = buffer.getHeight();
        double highestX = 0;
        double highestY = 0;

        for(int i = 0; i < points.length; i++) {
            double dx = points[i].getX() - center[0];
            double dy = points[i].getY() - center[1];
            double b = sqrt(pow(dx, 2) + pow(dy, 2));
            double originalAngle = atan2(dy, dx);
            double newAngle = originalAngle - angle;
            double newX = b * cos(newAngle) + center[0];
            double newY = b * sin(newAngle) + center[1];
            points[i] = new models.Point(newX, newY);

            if(points[i].getX() > highestX) {
                highestX = points[i].getX();
            }
            if(points[i].getY() > highestY) {
                highestY = points[i].getY();
            }
            if(points[i].getX() < lowestX) {
                lowestX = points[i].getX();
            }
            if(points[i].getY() < lowestY) {
                lowestY = points[i].getY();
            }
        }

        for(int i = 0; i < points.length; i++) {
            double newX = points[i].getX() - lowestX + 20;
            double newY = points[i].getY() - lowestY + 20;
            points[i] = new Point(newX, newY);
        }

        int bufferWidth = (int) floor(highestX - lowestX) + 40;
        int bufferHeight = (int) floor(highestY - lowestY) + 40;

        buffer = new CustomBuffer(bufferWidth, bufferHeight, buffer.getType(), this);

        build(buffer);
        return buffer;
    }
}
