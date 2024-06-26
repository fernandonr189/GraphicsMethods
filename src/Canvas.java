import builders.CircleBuilder;
import models.CustomBuffer;
import builders.SquareBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class Canvas extends JFrame implements Runnable{


    private final BufferedImage canvas;
    private final JPanel panel;
    private boolean backgroundPainted = false;
    private double t;
    private int seconds;

    private final double initialTime;

    CircleBuilder circleBuilder = new CircleBuilder();
    SquareBuilder squareBuilder = new SquareBuilder();
    CustomBuffer circleBuffer = new CustomBuffer(200, 200, BufferedImage.TYPE_INT_ARGB, circleBuilder);
    CustomBuffer squareBuffer = new CustomBuffer(200, 200, BufferedImage.TYPE_INT_ARGB, squareBuilder);

    public Canvas(int width, int height) {
        initialTime = (double) System.currentTimeMillis() / 1000;
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        setTitle("Metodos");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setSize(width, height);
        add(panel);


        setVisible(true);
        initializeEntities();
        new Thread(this).start();
    }

    private void initializeEntities() {
        circleBuffer.build();
        squareBuffer.build();
    }

    @Override
    public void paint(Graphics g) {
        if(!backgroundPainted) {
            super.paint(g);
            Graphics backgroundGraphics = canvas.getGraphics();
            backgroundGraphics.setColor(Color.black);
            backgroundGraphics.fillRect(0, 0, canvas.getHeight(), canvas.getWidth());
            backgroundPainted = true;
        }
        BufferedImage newBuffer = mergeBuffers();
        g.drawImage(newBuffer, 0, 0, panel);
    }


    @Override
    public void update(Graphics g) {
        super.update(g);
    }



    private boolean isGrowing = true;
    private boolean isScaling = true;
    private boolean isRotating = false;

    private BufferedImage mergeBuffers() {
        BufferedImage newImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();

        g2.drawImage(canvas, 0, 0, null);
        circleBuffer.draw(400, 400, g2);


        if(isScaling) {
            if(circleBuffer.isScaling()) {
                circleBuffer = circleBuffer.scale(t);
            }
            else {
                if(isGrowing) {
                    circleBuffer.setScaling(1.5, t, 1.0);
                }
                else {
                    circleBuffer.setScaling(0.5, t, 1.0);
                }
                isGrowing = !isGrowing;
            }
        }

        if(isRotating) {
            if(circleBuffer.isRotating()) {
                //circleBuffer = circleBuffer.rotate(t);
            }
            else {
                //circleBuffer.setRotating(2 * PI, t, 2.0);
            }
        }


        circleBuffer.movement(t, 400, 400, g2,
                (Double t) -> - (int) (300 * cos(t)),
                (Double t) -> (int) (300 * sin(t)));

        g2.dispose();
        return newImage;
    }

    @Override
    public void run() {
        int counter = 0;
        seconds = (int) floor(t);
        while(true) {
            try {
                repaint();
                Thread.sleep(6);
                t = (double) System.currentTimeMillis() / 1000 - initialTime;
                if((int) floor(t) > seconds) {
                    seconds = (int) floor(t);
                    if(seconds > 0) {
                        System.out.println(seconds + "s: " + counter + " frames -> " + (counter / seconds) + " fps " + "rebuilds: " + circleBuilder.rebuildCount / seconds);
                    }
                }
                counter++;
            } catch (InterruptedException e) {
                System.out.println("Frame: " + counter + " -> " + e);
                throw new RuntimeException(e);
            }
        }
    }
}