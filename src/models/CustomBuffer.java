package models;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

import static java.lang.Math.*;

public class CustomBuffer extends BufferedImage {

    private final BuildMethods builder;


    private double targetSize;
    private double targetTime;
    private double originalSize;
    private double originalTime;
    private boolean isScaling = false;
    private boolean isGrowing = false;


    private double targetAngle;
    private double targetTimeRotation;
    private double originalTimeRotation;
    private double previousAngle;
    private boolean isRotating = false;

    public CustomBuffer(int width, int height, int imageType, BuildMethods builder) {
        super(width, height, imageType);
        this.builder = builder;
    }

    public void build() {
        builder.build(this);
    }

    public void floodFill(int x, int y, Color a) {
        int targetColor = this.getRGB(x, y);
        if(targetColor == a.getRGB()) {
            return;
        }

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while(!queue.isEmpty()) {
            Point p = queue.poll();

            if(this.getRGB((int) p.getX(),(int) p.getY()) != targetColor ||
                p.getX() < 0 ||
                p.getY() < 0) {
                continue;
            }

            this.setRGB((int) p.getX(),(int) p.getY(), a.getRGB());
            queue.add(new Point(p.getX() - 1, p.getY()));
            queue.add(new Point(p.getX() + 1, p.getY()));
            queue.add(new Point(p.getX(), p.getY() - 1));
            queue.add(new Point(p.getX(), p.getY() + 1));
        }
    }


    public void basicCircle(int xc, int yc, int r, Color a) {
        int x1 = xc - r;
        int x2 = xc + r;
        for (int x = x1; x <= x2; x++) {
            double temp = sqrt(Math.pow(r, 2) - Math.pow((x - xc), 2));
            double ya = yc + temp;
            double yb = yc - temp;
            this.setRGB(x, (int) ya, a.getRGB());
            this.setRGB(x, (int) yb, a.getRGB());
        }
        int y1 = yc - r;
        int y2 = yc + r;
        for (int y = y1; y <= y2; y++) {
            double temp = sqrt(Math.pow(r, 2) - Math.pow((y - yc), 2));
            double xa = xc + temp;
            double xb = xc - temp;
            pixel((int) xa, y, a, 255);
            pixel((int) xb, y, a, 255);
        }
    }
    public void BresenhamLine(int x1, int y1, int x2, int y2, Color a) {
        int dy = y2 - y1;
        int dx = x2 - x1;
        int x = x1;
        int y = y1;
        int p;
        int incX = 1;
        int incY = 1;

        if (dy < 0){
            dy = -dy;
            incY = -1;
        }
        if (dx < 0){
            dx = -dx;
            incX = -1;
        }

        if (dx > dy){
            p = 2 * dy - dx;
            for (int i = 0; i < dx; i++){
                if (p >= 0){
                    y += incY;
                    p += 2 * (dy - dx);
                }
                else {
                    p += 2 * dy;
                }
                x += incX;
                pixel(x, y, a, 255);
            }
        }
        else {
            p = 2 * dx - dy;
            for (int i = 0; i < dy; i++){
                if (p >= 0){
                    x += incX;
                    p += 2 * (dx - dy);
                }
                else {
                    p += 2 * dx;
                }
                y += incY;
                pixel(x, y, a, 255);
            }
        }
    }

    public void DDALine(int x1, int y1, int x2, int y2, Color a) {
        int dy = y2 - y1;
        int dx = x2 - x1;
        double m = (double) dy / dx;

        if (x1 > x2 || y1 > y2) {
            int tempX = x1;
            int tempY = y1;
            x1 = x2;
            y1 = y2;
            x2 = tempX;
            y2 = tempY;
        }

        if (abs(m) <= 1){
            double y = y1;
            for(int x = x1; x <= x2; x++){
                pixel(x, (int) y, a, 255);
                y += m;
            }
        }
        else {
            double x = x1;
            for(int y = y1; y <= y2; y++){
                pixel((int) x, y, a, 255);
                x += (1/m);
            }
        }
    }

    public boolean isScaling() {
        return this.isScaling;
    }

    public boolean isRotating() {
        return this.isRotating;
    }

    public void setScaling(int targetSize, double initialTime, double timeDelta) {
        this.isScaling = true;
        this.targetSize = targetSize;
        this.targetTime = initialTime + timeDelta;
        this.originalSize = this.getWidth();
        this.originalTime = initialTime;
        isGrowing = targetSize > originalSize;
    }



    public void resumeScaling(double targetSize, double targetTime, double originalSize, double originalTime) {
        this.isScaling = true;
        this.targetSize = targetSize;
        this.targetTime = targetTime;
        this.originalSize = originalSize;
        this.originalTime = originalTime;
        isGrowing = targetSize > originalSize;
    }

    public CustomBuffer scale(double t) {
        if(isScaling) {

            double m = (targetSize - originalSize) / (targetTime - originalTime);
            double b = originalSize - (m * originalTime);
            double newSize = m * (t) + b;
            double factor = newSize / (double) this.getWidth();

            if(isGrowing && (int) floor(newSize) >= targetSize) {
                this.isScaling = false;
                return this;
            }
            if(!isGrowing && (int) floor(newSize) <= targetSize) {
                this.isScaling = false;
                return this;
            }

            CustomBuffer scaledBuffer = builder.scale(this, factor, true);
            scaledBuffer.resumeScaling(targetSize, targetTime, originalSize, originalTime);
            return scaledBuffer;
        }
        return this;
    }

    public void setRotating(double targetAngle, double initialTime, double timeDelta) {
        this.isRotating = true;
        this.targetAngle = targetAngle;
        this.targetTimeRotation = initialTime + timeDelta;
        this.previousAngle = 0;
        this.originalTimeRotation = initialTime;
    }

    private void resumeRotation(double targetAngle, double targetTime, double originalTime, double previousAngle) {
        this.isRotating = true;
        this.targetAngle = targetAngle;
        this.targetTimeRotation = targetTime;
        this.originalTimeRotation = originalTime;
        this.previousAngle = previousAngle;
    }

    public CustomBuffer rotate(double t){
        if(isRotating) {
            double angleDelta = 0;
            double m = (targetAngle) / (targetTimeRotation - originalTimeRotation);
            double newAngle = m * (t - originalTimeRotation);

            if(newAngle > targetAngle) {
                newAngle = targetAngle;
                angleDelta = newAngle - previousAngle;
                isRotating = false;
                return builder.rotate(this, angleDelta, true);
            }
            angleDelta = newAngle - previousAngle;

            CustomBuffer rotatedBuffer = builder.rotate(this, angleDelta, true);
            rotatedBuffer.resumeRotation(targetAngle, targetTimeRotation, originalTimeRotation, newAngle);
            return rotatedBuffer;
        }
        return this;
    }

    // this method must be called repeatedly in a period of time to create a movement
    public void movement(double t, int x0, int y0, Graphics graphics, Function<Double, Integer> xParam, Function<Double, Integer> yParam) {
        int x = xParam.apply(t) + x0 - this.getWidth() / 2;
        int y = yParam.apply(t) + y0 - this.getHeight() / 2;

        if(!graphics.drawImage(this, x, y, null)) {
            System.out.println("Not drawn");
        }
    }

    public void draw(int x, int y, Graphics graphics) {
        if(!graphics.drawImage(this, x - this.getWidth() / 2, y - this.getHeight() / 2, null)) {
            System.out.println("Not drawn");
        }
    }

    public void pixel(int x, int y, Color a, int alpha) {
        this.setRGB(x, y, new Color(a.getRed(), a.getGreen(), a.getBlue(), alpha).getRGB());
    }
}