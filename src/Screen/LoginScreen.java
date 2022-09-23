package Screen;

import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LoginScreen extends JPanel implements Runnable {

    private BufferedImage back;
    private ImageIcon logo;

    public LoginScreen() {
        logo = new ImageIcon();
        new Thread(this).start();
        Images images = new Images();
        logo = images.loadImage("logo.png");
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
        g2d.clearRect(0,0,getSize().width, getSize().height);

        g2d.drawImage(logo.getImage(), (getWidth()/2)-(logo.getIconWidth()/4), 10, logo.getIconWidth()/2, logo.getIconHeight()/2, this);

        twoDgraph.drawImage(back, null, 0, 0);
    }

}