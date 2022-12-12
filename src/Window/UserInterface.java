package Window;

import GoogleApi.Authentication;
import GoogleApi.GoogleDrive;
import GuiElements.Images;
import GuiElements.Button;
import GuiElements.SelectFolderButton;
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
import java.util.List;

public class UserInterface extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    private BufferedImage back;
    private Color backgroundColor;

    private Color customBlue = new Color(0, 71, 255);
    private ImageIcon logo, settingsIcon;
    private Settings settings;

    private SelectFolderButton selectFolderButtonUnapproved;
    private SelectFolderButton selectFolderButtonApproved;
    private static final GetResources resources = new GetResources();
    private final Authentication authentication = new Authentication();

    private File settingsFile = new File(resources.getFileResource("credentials.json").getPath().replace("credentials.json", "user/UserSettings.txt"));

    private final GoogleDrive googleDrive = new GoogleDrive();

    private int mouseX, mouseY;

    private boolean loginScreen, playerScreen, folderSelectionScreen, drawConnectionError, browserOpen, displaySettingsDropDown;

    private Font textFont = new Font("Berlin Sans FB", Font.PLAIN, 36);

    private List<com.google.api.services.drive.model.File> googleDriveFoldersListUnapproved;
    private List<com.google.api.services.drive.model.File> googleDriveFoldersListApproved;

    private JTextField unapprovedFileSearchJTextField;
    private JTextField approvedFileSearchJTextField;

    private Button loginButton = new Button(new Color(0, 71, 255), 243, 320, 150, 40, textFont, new Color(113, 149, 255), "Login", 30, 281, 348);
    private Button unaprovedSearchButton = new Button(new Color(0, 71, 255), 246, 200, 24, 24, textFont, new Color(113, 149, 255), "", 30, 281, 348);
    private Button aprovedSearchButton = new Button(new Color(0, 71, 255), 600, 200, 24, 24, textFont, new Color(113, 149, 255), "", 30, 281, 348);
    private Button settingsButton = new Button(new Color(70, 121, 255), 595, 5, 30, 30, new Font("Berlin Sans FB", Font.PLAIN, 20), new Color(115, 153, 255), "", 30, 281, 348);
    private SettingsDropDown settingsDropDown;

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

        settings = new Settings();
        settingsDropDown = new SettingsDropDown(new Color(70, 121, 255), 425, 5, 200, 175, settings.getSettingValue("theme"));

        setThemeOnStartup();

        logo = images.loadImage("logo.png");
        settingsIcon = images.loadImage("gear.png");

        unapprovedFileSearchJTextField = new JTextField();
        unapprovedFileSearchJTextField.setColumns(16);

        approvedFileSearchJTextField = new JTextField();
        approvedFileSearchJTextField.setColumns(16);

        unapprovedFileSearchJTextField.setBounds(10, 200, 236,24);
        approvedFileSearchJTextField.setBounds(364, 200, 236,24);

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

            g2d.setColor(customBlue);
            g2d.setFont(textFont);

            String loginText = "Sign in with Google:";

            g2d.drawString(loginText, (getWidth()/2)-((loginText.length()*((textFont.getSize())/5)+6)), getHeight()/4);

            g2d.drawImage(logo.getImage(), (getWidth()/2)-(logo.getIconWidth()/4), 10, logo.getIconWidth()/2, logo.getIconHeight()/2, this);

            loginButton.drawButton(g2d, mouseX, mouseY);

            drawFeedbackText(g2d);

            if (browserOpen) {
                g2d.setColor(customBlue);
                g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 24));
                g2d.drawString("Waiting for authentication...", 176,400);
            }
        } else if (playerScreen) {
            g2d.setColor(customBlue);
            g2d.drawString("PlayerScreen",100,100);
        } else if (folderSelectionScreen) {
            g2d.setColor(customBlue);
            g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 48));
            g2d.drawString("Folder Selection",175,100);
            g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 10));

            unaprovedSearchButton.drawButton(g2d, mouseX, mouseY);
            aprovedSearchButton.drawButton(g2d, mouseX, mouseY);

            g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 8));
            g2d.setColor(Color.BLUE);
            //g2d.drawString(googleDriveFoldersUnapproved.toString(), 100,300);
            //g2d.drawString(googleDriveFoldersApproved.toString(), 100, 325);
            //unapprovedFileSearchJTextField.setBounds(10, 200, 236,24);
            //approvedFileSearchJTextField.setBounds(364, 200, 236,24);

            selectFolderButtonUnapproved.drawSelectFolderButtons(g2d, googleDriveFoldersListUnapproved, mouseX, mouseY);
            selectFolderButtonApproved.drawSelectFolderButtons(g2d, googleDriveFoldersListApproved, mouseX, mouseY);
            }

        if (!loginScreen) {
            try {
                drawSettingsDropDown(g2d, mouseX, mouseY);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            settingsButton.drawButton(g2d, mouseX, mouseY);
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

    private void drawSettingsDropDown(Graphics g2d, int mouseX, int mouseY) throws FileNotFoundException {
        if (displaySettingsDropDown) {
            settingsDropDown.drawSettingsDropDown(g2d, mouseX, mouseY);
        }
    }

    private void drawFeedbackText(Graphics g2d) {
        if (drawConnectionError) {
            g2d.setColor(new Color(255,0,0));
            g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 24));
            g2d.drawString("No internet connection.", 200,400);
        }
    }

    public void googleSignIn() {

        if (browserOpen) {

            try {
                googleDriveFoldersListUnapproved = googleDrive.listFolders(authentication.logon(), "");
                googleDriveFoldersListApproved = googleDrive.listFolders(authentication.logon(), "");
                selectFolderButtonUnapproved = new SelectFolderButton(googleDriveFoldersListUnapproved, 10, 225, 236, 30, customBlue, new Color(113, 149, 255), textFont, 20, 20, 230, mouseX, mouseY, 10, 3);
                selectFolderButtonApproved = new SelectFolderButton(googleDriveFoldersListApproved, 364, 225, 236, 30, customBlue, new Color(133, 149, 255), textFont, 20, 376, 230, mouseX, mouseY, 10, 3);
            } catch (IOException ex) {
                ex.printStackTrace();
                authentication.logout();
            }

            try {
                authentication.logon();
            } catch (Exception exception) {
                exception.printStackTrace();
            }


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
            settingsDropDown.setThemeText("dark");
            backgroundColor = new Color(0,0,0);
        } else if (settings.getSettingValue("theme").equals("dark")) {
            settings.setSetting("theme", "light");
            settingsDropDown.setThemeText("light");
            backgroundColor = new Color(255,255,255);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getButton()!=1) return;

        //Login button
        if ( loginScreen && loginButton.isClicked(e.getX(), e.getY()) ) {
            if (checkInternetConnection()) {
                drawConnectionError = false;
                browserOpen = true;
                googleSignIn();
            } else {
                drawConnectionError = true;
            }
        }

        if (folderSelectionScreen) {

            if ( unaprovedSearchButton.isClicked(e.getX(), e.getY()) ) {
                //Unapproved Folder Search Button
                //System.out.println("Searching for folders: " + unapprovedFileSearchJTextField.getText());
                try {
                    googleDriveFoldersListUnapproved = googleDrive.listFolders(authentication.logon(), unapprovedFileSearchJTextField.getText());
                    selectFolderButtonUnapproved = new SelectFolderButton(googleDriveFoldersListUnapproved, 10, 225, 236, 30, customBlue, new Color(113, 149, 255), textFont, 20, 20, 230, mouseX, mouseY, 10, 3);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if ( aprovedSearchButton.isClicked(e.getX(), e.getY()) ) {
                //Approved Folder Search Button
                //System.out.println("Searching for folders: " + approvedFileSearchJTextField.getText());
                try {
                    googleDriveFoldersListApproved = googleDrive.listFolders(authentication.logon(), approvedFileSearchJTextField.getText());
                    selectFolderButtonApproved = new SelectFolderButton(googleDriveFoldersListApproved, 364, 225, 236, 30, customBlue, new Color(133, 149, 255), textFont, 20, 376, 230, mouseX, mouseY, 10, 3);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            //check Folder Click Unapproved Section!
            if (selectFolderButtonUnapproved.returnButtonClicked(googleDriveFoldersListUnapproved, e.getX(), e.getY()) != null) {
                System.out.println(selectFolderButtonUnapproved.returnButtonClicked(googleDriveFoldersListUnapproved, e.getX(), e.getY()));
            }
            //check Folder Click approved Section!
            if (selectFolderButtonApproved.returnButtonClicked(googleDriveFoldersListApproved, e.getX(), e.getY()) != null) {
                System.out.println(selectFolderButtonApproved.returnButtonClicked(googleDriveFoldersListApproved, e.getX(), e.getY()));
            }

        }

        //Open when settings button is pressed. When open close only when clicking button or area outside menu.
        if (!loginScreen && !displaySettingsDropDown && settingsButton.isClicked(e.getX(), e.getY()) ) {
            displaySettingsDropDown = true;
        } else if ( !(((e.getX() > settingsDropDown.getX()) && (e.getX() < settingsDropDown.getX()+settingsDropDown.getWidth())) && ((e.getY() > settingsDropDown.getY()) && (e.getY() < settingsDropDown.getY()+settingsDropDown.getHeight())))
                || settingsButton.isClicked(e.getX(), e.getY())
        ) {
            displaySettingsDropDown = false;
        }

        if (!loginScreen && displaySettingsDropDown) {
            if ( settingsDropDown.buttonClicked("Theme", e.getX(), e.getY()) ) {
                //Theme Button
                try {
                    setTheme();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else if ( settingsDropDown.buttonClicked("MusicPlayer", e.getX(), e.getY()) ){
                //Music Player Button
                playerScreen = true;
                folderSelectionScreen = false;

                remove(unapprovedFileSearchJTextField);
                remove(approvedFileSearchJTextField);

            } else if ( settingsDropDown.buttonClicked("FolderSelection", e.getX(), e.getY()) ) {
                //Folder Selection Button
                playerScreen = false;
                folderSelectionScreen = true;

                add(unapprovedFileSearchJTextField);
                add(approvedFileSearchJTextField);


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