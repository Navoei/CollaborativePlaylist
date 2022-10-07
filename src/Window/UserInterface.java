package Window;

import GoogleApi.Authentication;
import GoogleApi.Drive;
import GuiElements.Images;
import GuiElements.Button;
import GuiElements.SettingsDropDown;
import resources.GetResources;
import resources.Settings;

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

public class UserInterface extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    private BufferedImage back;
    private Color backgroundColor;
    private ImageIcon logo, settingsIcon;
    private Button button;
    private SettingsDropDown settingsDropDown;
    private Settings settings;
    private static final GetResources resources = new GetResources();
    private final Authentication authentication = new Authentication();

    private File settingsFile = new File(resources.getFileResource("credentials.json").getPath().replace("credentials.json", "user/UserSettings.txt"));

    private final Drive googleDrive = new Drive();

    private int mouseX, mouseY;

    private boolean loginScreen, playerScreen, folderSelectionScreen, drawConnectionError, browserOpen, displaySettingsDropDown;

    private Font textFont = new Font("Berlin Sans FB", Font.PLAIN, 36);

    private java.util.List<com.google.api.services.drive.model.File> googleDriveFolders;

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
        settings = new Settings();
        settingsDropDown = new SettingsDropDown();

        setThemeOnStartup();

        logo = images.loadImage("logo.png");
        settingsIcon = images.loadImage("gear.png");

        /*usernameJTextField = new JTextField();
        usernameJTextField.setColumns(16);
        add(usernameJTextField);

        passwordJTextField = new JPasswordField();
        passwordJTextField.setColumns(16);
        add(passwordJTextField);*/

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

    private void setThemeOnStartup() throws FileNotFoundException {
        if (settings.getSettingValue("theme").equals("light")) {
            backgroundColor = new Color(255, 255, 255);
        } else if (settings.getSettingValue("theme").equals("dark")) {
            backgroundColor = new Color(0, 0, 0);
        }
    }

    //public void paint(Graphics g) {
    public void paintComponent(Graphics g) {
        Graphics2D twoDgraph = (Graphics2D) g;
        if (back == null) {
            back = (BufferedImage) ((createImage(getWidth(), getHeight())));
        }
        Graphics g2d = back.createGraphics();
        g2d.setColor(backgroundColor);
        g2d.fillRect(0,0,getSize().width, getSize().height);

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
        } else if (playerScreen) {
            g2d.setColor(Color.BLUE);
            g2d.drawString("PlayerScreen",100,100);
        } else if (folderSelectionScreen) {
            g2d.setColor(Color.BLUE);
            g2d.drawString("FolderSelection",100,100);
            g2d.drawString(googleDriveFolders.toString(),0,200);
        }

        if (!loginScreen) {
            try {
                drawSettingsDropDown(g2d, new Color(0, 71, 255));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
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

    private void drawSettingsDropDown(Graphics g2d, Color color) throws FileNotFoundException {
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
            googleDriveFolders = googleDrive.listFolders(authentication.logon());

            //If authentication successful get rid of login screen
            loginScreen = false;
            browserOpen = false;
            playerScreen = true;

        }

    }

    private void googleSignOut() {
        authentication.logout();
        loginScreen = true;
    }

    private void setTheme() throws IOException {
        if (settings.getSettingValue("theme").equals("light")) {
            settings.setSetting("theme", "dark");
            backgroundColor = new Color(0,0,0);
        } else if (settings.getSettingValue("theme").equals("dark")) {
            settings.setSetting("theme", "light");
            backgroundColor = new Color(255,255,255);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getButton()!=1) return;

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

        //Open when settings button is pressed. When open close only when clicking button or area outside menu.
        if (!loginScreen && !displaySettingsDropDown && (((e.getX() > 595) && (e.getX() < 625)) && ((e.getY() > 5) && (e.getY() < 35))) ) {
            displaySettingsDropDown = true;
        } else if ( (((e.getX() < 425) || (e.getX() > 625)) || ((e.getY() < 5) || (e.getY() > 305)))
                    || (((e.getX() > 595) && (e.getX() < 625)) && ((e.getY() > 5) && (e.getY() < 35)))
        ) {
            displaySettingsDropDown = false;
        }


        if (!loginScreen && displaySettingsDropDown) {
            //Theme button
            if ( (((e.getX() > 435) && (e.getX() < 585)) && ((e.getY() > 35) && (e.getY() < 60))) ) {
                try {
                    setTheme();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //Folder Selection Button
            } else if ( (((e.getX() > 435) && (e.getX() < 585)) && ((e.getY() > 65) && (e.getY() < 90))) ){
                playerScreen = true;
                folderSelectionScreen = false;
            } else if ( (((e.getX() > 435) && (e.getX() < 585)) && ((e.getY() > 95) && (e.getY() < 120))) ) {
                playerScreen = false;
                folderSelectionScreen = true;
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