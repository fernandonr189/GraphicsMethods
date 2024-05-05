import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Canvas extends JFrame implements Runnable{


    private final BufferedImage canvas;
    private final JPanel panel;
    private boolean backgroundPainted = false;
    private double t;

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

    @Override
    public void paint(Graphics g) {
        if(!backgroundPainted) {
            super.paint(g);
            Graphics backgroundGraphics = canvas.getGraphics();
            backgroundGraphics.setColor(Color.black);
            backgroundGraphics.fillRect(0, 0, canvas.getHeight(), canvas.getWidth());
            backgroundPainted = true;
            g.drawImage(canvas, 0, 0, panel);
        }
        g.drawImage(canvas, 0, 0, null);
        CustomBuffer circleBuffer = new CustomBuffer(200, 200, BufferedImage.TYPE_INT_ARGB);
        circleBuffer.basicCircle(100, 100,80, Color.blue);
        circleBuffer.floodFill(100, 100, Color.blue);
        circleBuffer.DDALine(20, 20, 180, 180, Color.blue);
        circleBuffer.movement(t, 300, 300, getGraphics(), panel,
            (Double t) -> (int) (250 * cos(t)),
            (Double t) -> (int) (250 * sin(t)));
    }


    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    @Override
    public void run() {
        int counter = 0;
        t = 0;
        while(true) {
            try {
                repaint();
                Thread.sleep(8);
                t = (double) System.currentTimeMillis() / 1000;
                counter++;
            } catch (InterruptedException e) {
                System.out.println("Frame: " + counter + " -> " + e);
                throw new RuntimeException(e);
            }
        }
    }
}