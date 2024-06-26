package models;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

import static java.lang.Math.*;

public class CustomBuffer extends BufferedImage {

    private final BuildMethods builder;
    
    private Transformations transformations = new Transformations();
    
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
        return transformations.isScaling();
    }

    public boolean isRotating() {
        return transformations.isRotating();
    }

    @SuppressWarnings("unused")
    private void resumeTransformations(Transformations _transformations) {
        this.transformations = _transformations;
    }

    public void resumeScaling(Scaler scaler) {
        transformations.setScaling(true);
        transformations.setScaler(scaler);
    }
    
    public void setScaling(double _targetScale, double _initialTime, double _time) {
        transformations.setScaling(true);
        double currentScale = transformations.getCurrentScale();
        transformations.setScaler(new Scaler(_targetScale, _initialTime, _time, currentScale));
    }

    public CustomBuffer scale(double t) {
        if(transformations.isScaling()) {
            Scaler scaler = transformations.getScaler();
            double newScale = scaler.newScale(t);
            transformations.setCurrentScale(newScale);

            if(scaler.isFinished()) {
                transformations.setScaling(false);
                return this;
            }

            CustomBuffer scaledBuffer = builder.scale(this, newScale, true);
            scaledBuffer.resumeScaling(scaler);
            return scaledBuffer;
        }
        else {
            return this;
        }
    }

    private void resumeRotation(Rotator rotator, double previousAngle) {
        transformations.setRotating(true);
        transformations.setCurrentAngle(previousAngle);
        transformations.setRotator(rotator);
    }

    public void setRotating(double _targetAngle, double _initialTime, double _time) {
        transformations.setRotating(true);
        transformations.setRotator(new Rotator(_targetAngle, _initialTime, _time));
    }

    public CustomBuffer rotate(double t){
        if(transformations.isRotating()) {
            Rotator rotator = transformations.getRotator();
            double currentAngle = transformations.getCurrentAngle();
            double angleDelta = 0;
            double newAngle = rotator.newAngle(t);

            if(rotator.isFinished()) {
                newAngle = rotator.getTargetAngle();
                angleDelta = newAngle - currentAngle;
                transformations.setRotating(false);
                return builder.rotate(this, angleDelta, true);
            }
            angleDelta = newAngle - currentAngle;

            CustomBuffer rotatedBuffer = builder.rotate(this, angleDelta, true);
            rotatedBuffer.resumeRotation(rotator, newAngle);
            return rotatedBuffer;
        }
        return this;
    }

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