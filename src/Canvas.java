import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class Canvas extends JFrame implements Runnable{


    private final BufferedImage canvas;
    private final JPanel panel;
    private boolean backgroundPainted = false;
    private double t;

    CustomBuffer circleBuffer = new CustomBuffer(200, 200, BufferedImage.TYPE_INT_ARGB);
    CustomBuffer squareBuffer = new CustomBuffer(200, 200, BufferedImage.TYPE_INT_ARGB);

    public Canvas(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        setTitle("Metodos");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setSize(width, height);
        add(panel);


        setVisible(true);

        new Thread(this).start();
    }

    private void initializeEntities() {
        circleBuffer.basicCircle(100, 100,80, Color.blue);
        circleBuffer.floodFill(100, 100, Color.blue);
        circleBuffer.DDALine(20, 20, 180, 180, Color.blue);

        squareBuffer.BresenhamLine(20, 20, 180, 20, Color.red);
        squareBuffer.BresenhamLine(180, 20, 180, 180, Color.red);
        squareBuffer.BresenhamLine(180,180, 20, 180, Color.red);
        squareBuffer.BresenhamLine(20,180, 20, 20, Color.red);
        squareBuffer.floodFill(21, 21, Color.red);

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

    private BufferedImage mergeBuffers() {
        BufferedImage newImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();

        g2.drawImage(canvas, 0, 0, null);

        circleBuffer.movement(t, 300, 300, g2,
                (Double t) -> (int) (250 * cos(t * 2)),
                (Double t) -> (int) (250 * sin(t * 10)));

        squareBuffer.movement(t, 300, 300, g2,
                (Double t) -> - (int) (250 * cos(t)),
                (Double t) -> 255);

        squareBuffer.movement(t, 300, 300, g2,
                (Double t) -> 255,
                (Double t) -> (int) (250 * sin(t * 4)));

        g2.dispose();
        return newImage;
    }

    @Override
    public void run() {
        initializeEntities();
        int counter = 0;
        t = 0;
        while(true) {
            try {
                repaint();
                Thread.sleep(6);
                t = (double) System.currentTimeMillis() / 1000;
                counter++;
            } catch (InterruptedException e) {
                System.out.println("Frame: " + counter + " -> " + e);
                throw new RuntimeException(e);
            }
        }
    }
}