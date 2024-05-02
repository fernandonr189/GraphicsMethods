import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JFrame {


    private BufferedImage canvas;
    private JPanel panel;


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
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //Methods.DDALine(100, 100, 200, 100, Color.red, canvas);
        //Methods.DDALine(200, 100, 200, 200, Color.red, canvas);
        //Methods.DDALine(200, 200, 100, 200, Color.red, canvas);
        //Methods.DDALine(100, 200, 100, 100, Color.red, canvas);
        Methods.basicCircle(150, 150, 110, Color.red, canvas);
        Methods.floodFill(150, 150, Color.red, canvas);
        getGraphics().drawImage(canvas, 0, 0, panel);
    }
}