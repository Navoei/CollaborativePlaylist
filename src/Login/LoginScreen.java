package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LoginScreen extends JPanel implements Runnable {

    private BufferedImage back;

    public LoginScreen() {
        new Thread(this).start();
    }

    public void run() {
        try {
            while(true) {
                Thread.currentThread().sleep(1);
                repaint();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        Graphics2D twoDgraph = (Graphics2D) g;
        if (back == null) {
            back = (BufferedImage) ((createImage(getWidth(), getHeight())));
        }
        Graphics g2d = back.createGraphics();
        g2d.setColor(new Color(0,0,0));
        g2d.fillOval(100,100,50,50);

        twoDgraph.drawImage(back, null, 0, 0);
    }

}