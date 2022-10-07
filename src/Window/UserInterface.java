package Window;

import GoogleApi.Authentication;
import GoogleApi.Drive;
import GuiElements.Images;
import GuiElements.Button;
import GuiElements.SettingsDropDown;
import resources.GetResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.Scanner;

public class UserInterface extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    private BufferedImage back;
    private ImageIcon logo, settingsIcon;
    private Button button;
    private SettingsDropDown settingsDropDown;
    private static final GetResources resources = new GetResources();
    private final Authentication authentication = new Authentication();

    private File settingsFile = new File(resources.getFileResource("credentials.json").getPath().replace("credentials.json", "user/UserSettings.txt"));

    private final Drive googleDrive = new Drive();

    private int mouseX, mouseY;

    private boolean loginScreen, playerScreen, drawConnectionError, browserOpen, displaySettingsDropDown;

    private Font textFont = new Font("Berlin Sans FB", Font.PLAIN, 36);

    //private JTextField usernameJTextField;
    //private JPasswordField passwordJTextField;

    public UserInterface() throws GeneralSecurityException, IOException {

        new Thread(this).start();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        loginScreen = true;
        playerScreen = false;
        drawConnectionError = false;
        browserOpen = false;
        displaySettingsDropDown = false;

        Images images = new Images();
        button = new Button();
        settingsDropDown = new SettingsDropDown();

        logo = images.loadImage("logo.png");
        settingsIcon = images.loadImage("gear.png");

        /*usernameJTextField = new JTextField();
        usernameJTextField.setColumns(16);
        add(usernameJTextField);

        passwordJTextField = new JPasswordField();
        passwordJTextField.setColumns(16);
        add(passwordJTextField);*/

    }

    public String getTheme() throws FileNotFoundException {
        Scanner scanner = new Scanner(settingsFile);
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
    }

    public void setTheme(String theme) {
        if (theme=="light") {

        } else if (theme=="dark") {

        }
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

        if (loginScreen) {
            g2d.setColor(new Color(0, 71, 255));
            g2d.setFont(textFont);

            String loginText = "Sign in with Google:";

            //usernameJTextField.setBounds(182, 223, 270,37);
            //passwordJTextField.setBounds(182, 323, 270,37);

            g2d.drawString(loginText, (getWidth()/2)-((loginText.length()*((textFont.getSize())/5)+6)), getHeight()/4);

            g2d.drawImage(logo.getImage(), (getWidth()/2)-(logo.getIconWidth()/4), 10, logo.getIconWidth()/2, logo.getIconHeight()/2, this);

            button.drawButton(g2d, new Color(0, 71, 255), 243, 320, 150, 40, textFont, new Color(113, 149, 255), "Login", 30, 281, 348, mouseX, mouseY);

            drawFeedbackText(g2d);

            if (browserOpen) {
                g2d.setColor(new Color(0, 187, 0));
                g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 24));
                g2d.drawString("Waiting for authentication...", 176,400);
            }
        } else if (!loginScreen && playerScreen) {
            drawSettingsDropDown(g2d, new Color(0, 71, 255));
            button.drawButton(g2d, new Color(0, 71, 255), 595, 5, 30, 30, new Font("Berlin Sans FB", Font.PLAIN, 20), new Color(113, 149, 255), "", 30, 281, 348, mouseX, mouseY);
            g2d.drawImage(settingsIcon.getImage(), 600, 10, 20, 20, this);
        }

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

    private void drawSettingsDropDown(Graphics g2d, Color color) {
        if (displaySettingsDropDown) {
            settingsDropDown.drawSettingsDropDown(g2d, color, mouseX, mouseY);
        }
    }

    private void drawFeedbackText(Graphics g2d) {
        if (drawConnectionError) {
            g2d.setColor(new Color(255,0,0));
            g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 24));
            g2d.drawString("No internet connection.", 200,400);
        }
    }

    public void googleSignIn() throws IOException {

        if (browserOpen) {

            authentication.logon();

            //If authentication successful get rid of login screen
            loginScreen = false;
            browserOpen = false;
            playerScreen = true;

            printFilesInDrive();

        }

    }

    private void printFilesInDrive() throws IOException {
        googleDrive.listFolders(authentication.logon());
    }

    private void googleSignOut() {
        authentication.logout();
        loginScreen = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //Login button
        if ( loginScreen && (((e.getX() > 243) && (e.getX() < 393)) && ((e.getY() > 320) && (e.getY() < 360))) ) {
            if (checkInternetConnection()) {
                drawConnectionError = false;
                browserOpen = true;
                try {
                    googleSignIn();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                drawConnectionError = true;
            }
        }

        //Settings button
        if (!loginScreen && !displaySettingsDropDown && (((e.getX() > 595) && (e.getX() < 625)) && ((e.getY() > 5) && (e.getY() < 35))) ) {
            displaySettingsDropDown = true;
        } else {
            displaySettingsDropDown = false;
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