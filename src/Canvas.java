import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
        }

        getGraphics().drawImage(canvas, 0, 0, panel);

        BufferedImage circleBuffer = new BufferedImage(11, 11, BufferedImage.TYPE_INT_ARGB);

        Methods.BresenhamLine(1, 1, 9, 9, Color.red, circleBuffer);

        Methods.moveInCircles(t, 100, getGraphics(), circleBuffer, panel);
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
                Thread.sleep(1);
                t = (double) System.currentTimeMillis() / 1000;
                counter++;
            } catch (InterruptedException e) {
                System.out.println("Frame: " + counter + " -> " + e);
                throw new RuntimeException(e);
            }
        }
    }
}