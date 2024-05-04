import models.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Math.*;

public class CustomBuffer extends BufferedImage{


    public CustomBuffer(int width, int height, int imageType) {
        super(width, height, imageType);
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

            if(this.getRGB(p.getX(), p.getY()) == a.getRGB()) {
                continue;
            }

            this.setRGB(p.getX(), p.getY(), a.getRGB());
            queue.add(new Point(p.getX() - 1, p.getY()));
            queue.add(new Point(p.getX() + 1, p.getY()));
            queue.add(new Point(p.getY(), p.getX() - 1));
            queue.add(new Point(p.getY(), p.getX() + 1));
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
            pixel((int) xa, y, a, 255, this);
            pixel((int) xb, y, a, 255, this);
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
        double m = (double) dy / dx;

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
            for (int i = 0; i <= dx; i++){
                if (p >= 0){
                    y += incY;
                    p += 2 * (dy - dx);
                }
                else {
                    p += 2 * dy;
                }
                x += incX;
                pixel(x, y, a, 255, this);
            }
        }
        else {
            p = 2 * dx - dy;
            for (int i = 0; i <= dy; i++){
                if (p >= 0){
                    x += incX;
                    p += 2 * (dx - dy);
                }
                else {
                    p += 2 * dx;
                }
                y += incY;
                pixel(x, y, a, 255, this);
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
                pixel(x, (int) y, a, 255, this);
                y += m;
            }
        }
        else {
            double x = x1;
            for(int y = y1; y <= y2; y++){
                pixel((int) x, y, a, 255, this);
                x += (1/m);
            }
        }
    }

    // this method must be called repeatedly in a period of time to create a movement
    public void moveInCircles(double t, int r, int x1, int y1, Graphics graphics, JPanel panel) {
        double x = r * cos(t) + x1;
        double y = r * sin(t) + y1;

        graphics.drawImage(this, (int) floor(x), (int) floor(y), panel);
    }

    public static void pixel(int x, int y, Color a, int alpha, BufferedImage buffer) {
        Color current = new Color(buffer.getRGB(x, y), true);
        if(current.getAlpha() < alpha) {
            buffer.setRGB(x, y, new Color(a.getRed(), a.getGreen(), a.getBlue(), alpha).getRGB());
        }
    }
}
