package Screen;

import resources.Images;
import resources.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UserInterface extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    private BufferedImage back;
    private ImageIcon logo;
    private Button button;
    private int mouseX, mouseY;
    private boolean loginScreen;
    private JTextField usernameJTextField;
    private JPasswordField passwordJTextField;

    public UserInterface() {
        new Thread(this).start();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        loginScreen = true;

        Images images = new Images();
        button = new Button();

        logo = images.loadImage("logo.png");

        usernameJTextField = new JTextField();
        usernameJTextField.setColumns(16);
        add(usernameJTextField);

        passwordJTextField = new JPasswordField();
        passwordJTextField.setColumns(16);
        add(passwordJTextField);

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

    //public void paint(Graphics g) {
    public void paintComponent(Graphics g) {
        Graphics2D twoDgraph = (Graphics2D) g;
        if (back == null) {
            back = (BufferedImage) ((createImage(getWidth(), getHeight())));
        }
        Graphics g2d = back.createGraphics();
        g2d.clearRect(0,0,getSize().width, getSize().height);

        g2d.setColor(new Color(0, 71, 255));
        Font textFont = new Font("Berlin Sans FB", Font.PLAIN, 36);
        g2d.setFont(textFont);

        String loginText = "Sign in with Google:";

        usernameJTextField.setBounds(182, 223, 270,37);
        passwordJTextField.setBounds(182, 323, 270,37);

        g2d.drawString(loginText, (getWidth()/2)-((loginText.length()*((textFont.getSize())/5)+6)), getHeight()/4);

        g2d.drawImage(logo.getImage(), (getWidth()/2)-(logo.getIconWidth()/4), 10, logo.getIconWidth()/2, logo.getIconHeight()/2, this);

        button.drawButton(g2d, new Color(0, 71, 255), 243, 420, 150, 40, textFont, new Color(113, 149, 255), "Login", 30, 281, 448, mouseX, mouseY);

        twoDgraph.drawImage(back, null, 0, 0);
    }

    public boolean checkInternetConnection() {
        try {
            final URL url = new URL("https://www.google.com");
            final URLConnection connection = url.openConnection();
            connection.connect();
            connection.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }

    }

    public void loginToGoogleAccount() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if ( loginScreen && (((e.getX() > 243) && (e.getX() < 393)) && ((e.getY() > 420) && (e.getY() < 460))) ) {
            if (checkInternetConnection()) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Could not connect to Google services. Please try again.");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}