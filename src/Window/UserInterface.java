package Window;

import GoogleApi.Authentication;
import GoogleApi.FilesManager;
import GoogleApi.GoogleDrive;
import GuiElements.Button;
import GuiElements.*;
import javazoom.jl.decoder.JavaLayerException;
import resources.GetResources;
import resources.Settings;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;

public class UserInterface extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    private BufferedImage back;
    private Color backgroundColor;
    private Color customBlue = new Color(0, 71, 255);
    private ImageIcon logo, settingsIcon;
    private Settings settings;
    private FilesManager filesManager;
    private MusicPlayer player;

    private SelectFolderButton selectFolderButtonUnapproved;
    private SelectFolderButton selectFolderButtonApproved;
    private SelectMusicButton selectMusicButtonUnapproved;
    private SelectMusicButton selectMusicButtonApproved;
    private static final GetResources resources = new GetResources();
    private final Authentication authentication = new Authentication();

    private final GoogleDrive googleDrive = new GoogleDrive();

    private int mouseX, mouseY;

    private boolean loginScreen, playerScreen, folderSelectionScreen, playlistManagerScreen, drawConnectionError, browserOpen, displaySettingsDropDown, displayNoFolderSelectedError;

    private Font textFont = new Font("Berlin Sans FB", Font.PLAIN, 36);

    private JTextField unapprovedFileSearchJTextField;
    private JTextField approvedFileSearchJTextField;
    private Button loginButton = new Button(new Color(0, 71, 255), 243, 320, 150, 40, textFont, new Color(113, 149, 255), "Login", 30, 281, 348);
    private Button settingsButton = new Button(new Color(70, 121, 255), 595, 5, 30, 30, new Font("Berlin Sans FB", Font.PLAIN, 20), new Color(115, 153, 255), "", 30, 281, 348);
    private Button pauseButton = new Button(new Color(0, 71, 255), 299, 460, 40, 40, new Font("Apple Color Emoji", Font.PLAIN, 36), new Color(113, 149, 255), "\u25b6", 30, 306, 489);
    private Button skipButton = new Button(new Color(0, 71, 255), 347, 460, 40, 40, new Font("Apple Color Emoji", Font.PLAIN, 36), new Color(113, 149, 255), "\u23ed", 30, 354, 489);
    private Button previousButton = new Button(new Color(0, 71, 255), 251, 460, 40, 40, new Font("Apple Color Emoji", Font.PLAIN, 36), new Color(113, 149, 255), "\u23ee", 30, 258, 489);
    private SettingsDropDown settingsDropDown;
    private String selectedUnapprovedFolderName;
    private String selectedApprovedFolderName;

    private Thread listFilesThread;

    public UserInterface() throws GeneralSecurityException, IOException {

        new Thread(this).start();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        loginScreen = true;
        playerScreen = false;
        playlistManagerScreen = false;
        drawConnectionError = false;
        browserOpen = false;
        displaySettingsDropDown = false;
        displayNoFolderSelectedError = false;

        Images images = new Images();

        settings = new Settings();
        filesManager = new FilesManager();
        player = new MusicPlayer();
        settingsDropDown = new SettingsDropDown(new Color(70, 121, 255), 425, 5, 200, 175, settings.getSettingValue("theme"));

        setThemeOnStartup();

        logo = images.loadImage("logo.png");
        settingsIcon = images.loadImage("gear.png");

        if (!settings.getSettingValue("approvedplaylistfolder").equals("null")) {
            selectedUnapprovedFolderName = settings.getSettingValue("unapprovedplaylistfolder");
        }

        if (!settings.getSettingValue("unapprovedplaylistfolder").equals("null")) {
            selectedApprovedFolderName = settings.getSettingValue("approvedplaylistfolder");
        }

        unapprovedFileSearchJTextField = new JTextField();
        unapprovedFileSearchJTextField.setColumns(16);
        unapprovedFileSearchJTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                listFilesThread = new Thread(this::update);
                listFilesThread.start();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                listFilesThread = new Thread(this::update);
                listFilesThread.start();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                listFilesThread = new Thread(this::update);
                listFilesThread.start();
            }

            public void update() {
                try {
                    if (folderSelectionScreen) {
                        filesManager.setUnapprovedFoldersList(googleDrive.listFolders(authentication.logon(), unapprovedFileSearchJTextField.getText()));
                        selectFolderButtonUnapproved = new SelectFolderButton(filesManager.getUnapprovedFoldersList(), 10, 225, 236, 30, customBlue, new Color(113, 149, 255), textFont, 16, 20, 230, 10, 3);
                    }
                    if (playlistManagerScreen) {
                        updateUnapprovedMusic();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        approvedFileSearchJTextField = new JTextField();
        approvedFileSearchJTextField.setColumns(16);
        approvedFileSearchJTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                listFilesThread = new Thread(this::update);
                listFilesThread.start();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                listFilesThread = new Thread(this::update);
                listFilesThread.start();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                listFilesThread = new Thread(this::update);
                listFilesThread.start();
            }

            public void update() {
                try {
                    if (folderSelectionScreen) {
                        filesManager.setApprovedFoldersList(googleDrive.listFolders(authentication.logon(), approvedFileSearchJTextField.getText()));
                        selectFolderButtonApproved = new SelectFolderButton(filesManager.getApprovedFoldersList(), 364, 225, 236, 30, customBlue, new Color(133, 149, 255), textFont, 16, 376, 230, 10, 3);
                    }
                    if (playlistManagerScreen) {
                        updateApprovedMusic();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

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

    private void updateApprovedMusic() {
            try {
                filesManager.setApprovedMusicList(googleDrive.listMusicFilesInsideFolder(authentication.logon(), settings.getSettingValue("approvedplaylistfolder"), approvedFileSearchJTextField.getText()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            selectMusicButtonApproved = new SelectMusicButton(filesManager.getApprovedMusicList(), 364, 225, 206, 30, customBlue, new Color(133, 149, 255), textFont, 16, 376, 230, 10, 3, new Color(255, 40, 40), new Color(154, 0, 0), 30, 30, "\u274c", "\uD83D\uDDD1", new Color(149, 149, 149), new Color(84, 84, 84));
    }
    private void updateUnapprovedMusic() {
            try {
                filesManager.setUnapprovedMusicList(googleDrive.listMusicFilesInsideFolder(authentication.logon(), settings.getSettingValue("unapprovedplaylistfolder"), unapprovedFileSearchJTextField.getText()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            selectMusicButtonUnapproved = new SelectMusicButton(filesManager.getUnapprovedMusicList(), 10, 225, 206, 30, customBlue, new Color(113, 149, 255), textFont, 16, 20, 230, 10, 3, new Color(0, 217, 0), new Color(0, 162, 0), 30, 30, "\u2714", "\uD83D\uDDD1", new Color(149, 149, 149), new Color(84, 84, 84));
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
            pauseButton.drawButton(g2d, mouseX, mouseY);
            skipButton.drawButton(g2d, mouseX, mouseY);
            previousButton.drawButton(g2d, mouseX, mouseY);

            if (player.hasMusicStarted() && player.nowPlaying()!=null) {
                g2d.setColor(customBlue);
                String currentSongName = player.nowPlaying().getName();
                int stringWidth = g2d.getFontMetrics().stringWidth(currentSongName);
                int stringX = (getWidth() - stringWidth) / 2;
                g2d.drawString(currentSongName, stringX, 400);
            }

        } else if (folderSelectionScreen) {
            g2d.setColor(customBlue);
            g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 48));
            g2d.drawString("Folder Selection",175,100);
            g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 20));

            if (selectedUnapprovedFolderName==null) {
                g2d.drawString("Select Unapproved Music Folder", 10, 190);
            } else {
                if (selectedUnapprovedFolderName.length() > 10) {
                    g2d.drawString("Selected Folder: " + selectedUnapprovedFolderName.substring(0, 10) + "...", 10, 190);
                } else {
                    g2d.drawString("Selected Folder: " + selectedUnapprovedFolderName, 10, 190);
                }
            }

            if (selectedApprovedFolderName==null) {
                g2d.drawString("Select Approved Music Folder", 364, 190);
            } else {
                if (selectedApprovedFolderName.length() > 10) {
                    g2d.drawString("Selected Folder: " + selectedApprovedFolderName.substring(0, 10) + "...", 364, 190);
                } else {
                    g2d.drawString("Selected Folder: " + selectedApprovedFolderName, 364, 190);
                }
            }

            g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 8));
            g2d.setColor(Color.BLUE);
            //g2d.drawString(googleDriveFoldersUnapproved.toString(), 100,300);
            //g2d.drawString(googleDriveFoldersApproved.toString(), 100, 325);
            //unapprovedFileSearchJTextField.setBounds(10, 200, 236,24);
            //approvedFileSearchJTextField.setBounds(364, 200, 236,24);

            selectFolderButtonUnapproved.drawSelectFolderButtons(g2d, filesManager.getUnapprovedFoldersList(), mouseX, mouseY);
            selectFolderButtonApproved.drawSelectFolderButtons(g2d, filesManager.getApprovedFoldersList(), mouseX, mouseY);
        } else if (playlistManagerScreen) {
            g2d.setColor(customBlue);

            if (displayNoFolderSelectedError) {
                g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 34));
                g2d.drawString("Playlist folders not selected.",132,100);
            } else {
                g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 48));
                g2d.drawString("Playlist Manager",170,100);

                g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 20));
                g2d.drawString("Unapproved Music", 10, 190);
                g2d.drawString("Approved Music", 364, 190);

                selectMusicButtonUnapproved.drawSelectMusicButtons(g2d, filesManager.getUnapprovedMusicList(), mouseX, mouseY);
                selectMusicButtonApproved.drawSelectMusicButtons(g2d, filesManager.getApprovedMusicList(), mouseX, mouseY);
            }
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

    public void googleSignIn() throws IOException {

        if (browserOpen) {

            try {
                filesManager.setUnapprovedFoldersList(googleDrive.listFolders(authentication.logon(), ""));
                filesManager.setApprovedFoldersList(googleDrive.listFolders(authentication.logon(), ""));
                selectFolderButtonUnapproved = new SelectFolderButton(filesManager.getUnapprovedFoldersList(), 10, 225, 236, 30, customBlue, new Color(113, 149, 255), textFont, 16, 20, 230, 10, 3);
                selectFolderButtonApproved = new SelectFolderButton(filesManager.getApprovedFoldersList(), 364, 225, 236, 30, customBlue, new Color(133, 149, 255), textFont, 16, 376, 230, 10, 3);

                filesManager.setUnapprovedMusicList(googleDrive.listMusicFilesInsideFolder(authentication.logon(), settings.getSettingValue("unapprovedplaylistfolder"), ""));
                filesManager.setApprovedMusicList(googleDrive.listMusicFilesInsideFolder(authentication.logon(), settings.getSettingValue("approvedplaylistfolder"), ""));
                selectMusicButtonUnapproved = new SelectMusicButton(filesManager.getUnapprovedMusicList(), 10, 225, 206, 30, customBlue, new Color(113, 149, 255), textFont, 16, 20, 230, 10, 3, new Color(0, 217, 0), new Color(0, 162, 0), 30, 30, "\u2714", "\uD83D\uDDD1", new Color(149, 149, 149), new Color(84, 84, 84));
                selectMusicButtonApproved = new SelectMusicButton(filesManager.getApprovedMusicList(), 364, 225, 206, 30, customBlue, new Color(133, 149, 255), textFont, 16, 376, 230, 10, 3, new Color(255, 40, 40), new Color(154, 0, 0), 30, 30, "\u274c", "\uD83D\uDDD1", new Color(149, 149, 149), new Color(84, 84, 84));
            } catch (IOException e) {
                e.printStackTrace();
                authentication.logout();
            }

            try {
                authentication.logon();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //If authentication successful get rid of login screen
            loginScreen = false;
            browserOpen = false;
            playerScreen = true;
        }

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
                try {
                    googleSignIn();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                drawConnectionError = true;
            }
        }

        if (playerScreen) {

            if (pauseButton.isClicked(mouseX, mouseY)) {
                if (filesManager.getApprovedMusicList()==null) return;
                if (!player.hasMusicStarted()) {
                    player.playPlaylist();
                    pauseButton.setButtonText("\u23f8");
                    pauseButton.setButtonTextX(303);
                } else {
                    if (player.isPaused()) {
                        player.resumePlayer();
                        pauseButton.setButtonText("\u23f8");
                        pauseButton.setButtonTextX(303);

                    } else {
                        player.pausePlayer();
                        pauseButton.setButtonText("\u25b6");
                        pauseButton.setButtonTextX(306);
                    }
                }
            }

            if (skipButton.isClicked(mouseX, mouseY)) {
                if (filesManager.getApprovedMusicList()==null) return;
                if (!player.hasMusicStarted()) {
                    player.playPlaylist();
                    pauseButton.setButtonText("\u23f8");
                } else {
                    player.resumePlayer();
                    pauseButton.setButtonText("\u23f8");
                    pauseButton.setButtonTextX(303);
                    player.skipTrack();
                }
            }

            if (previousButton.isClicked(mouseX, mouseY)) {
                if (filesManager.getApprovedMusicList()==null) return;
                if (!player.hasMusicStarted()) {
                    player.playPlaylist();
                    pauseButton.setButtonText("\u23f8");
                } else {
                    player.resumePlayer();
                    pauseButton.setButtonText("\u23f8");
                    pauseButton.setButtonTextX(303);
                    player.toPreviousTrack();
                }
            }

        }

        if (folderSelectionScreen) {

            //check Folder Click Unapproved Section!
            if (selectFolderButtonUnapproved.returnButtonClicked(filesManager.getUnapprovedFoldersList(), e.getX(), e.getY()) != null) {
                try {
                    settings.setSetting("unapprovedplaylistfolder", selectFolderButtonUnapproved.returnButtonClicked(filesManager.getUnapprovedFoldersList(), e.getX(), e.getY()).getId());
                    selectedUnapprovedFolderName = googleDrive.getFileNameById(authentication.logon(), settings.getSettingValue("unapprovedplaylistfolder"));
                    filesManager.setUnapprovedMusicList(googleDrive.listMusicFilesInsideFolder(authentication.logon(), settings.getSettingValue("unapprovedplaylistfolder"), ""));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            //check Folder Click approved Section!
            if (selectFolderButtonApproved.returnButtonClicked(filesManager.getApprovedFoldersList(), e.getX(), e.getY()) != null) {
                //System.out.println(selectFolderButtonApproved.returnButtonClicked(googleDriveFoldersListApproved, e.getX(), e.getY()));
                try {
                    settings.setSetting("approvedplaylistfolder", selectFolderButtonApproved.returnButtonClicked(filesManager.getApprovedFoldersList(), e.getX(), e.getY()).getId());
                    selectedApprovedFolderName = googleDrive.getFileNameById(authentication.logon(), settings.getSettingValue("approvedplaylistfolder"));
                    filesManager.setApprovedMusicList(googleDrive.listMusicFilesInsideFolder(authentication.logon(), settings.getSettingValue("approvedplaylistfolder"), ""));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }

        if (playlistManagerScreen) {
            if (selectMusicButtonUnapproved.returnMoveFileButtonClicked(filesManager.getUnapprovedMusicList(), mouseX, mouseY) != null) {
                listFilesThread = new Thread(() -> {
                    try {
                        googleDrive.moveFileToAnotherFolder(authentication.logon(), selectMusicButtonUnapproved.returnMoveFileButtonClicked(filesManager.getUnapprovedMusicList(), mouseX, mouseY).getId(), settings.getSettingValue("approvedplaylistfolder"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    updateApprovedMusic();
                    updateUnapprovedMusic();
                });
                listFilesThread.start();
            }
            if (selectMusicButtonApproved.returnMoveFileButtonClicked(filesManager.getApprovedMusicList(), mouseX, mouseY) != null) {
                listFilesThread = new Thread(() -> {
                    try {
                        googleDrive.moveFileToAnotherFolder(authentication.logon(), selectMusicButtonApproved.returnMoveFileButtonClicked(filesManager.getApprovedMusicList(), mouseX, mouseY).getId(), settings.getSettingValue("unapprovedplaylistfolder"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    updateApprovedMusic();
                    updateUnapprovedMusic();
                });
                listFilesThread.start();
            }

            //if (selectMusicButtonUnapproved.returnDeleteFileButtonClicked(filesManager.getUnapprovedMusicList(), mouseX, mouseY) != null) {
            //
            //}
            //if (selectMusicButtonApproved.returnDeleteFileButtonClicked(filesManager.getApprovedMusicList(), mouseX, mouseY) != null) {
            //
            //}

            if (selectMusicButtonUnapproved.returnFileWhenButtonClicked(filesManager.getUnapprovedMusicList(), mouseX, mouseY) != null) {
                try {
                    if (!player.isPaused()) {
                        player.pausePlayer();
                        pauseButton.setButtonText("\u25b6");
                        pauseButton.setButtonTextX(306);
                    }
                    player.playFile(selectMusicButtonUnapproved.returnFileWhenButtonClicked(filesManager.getUnapprovedMusicList(), mouseX, mouseY));
                } catch (JavaLayerException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (selectMusicButtonApproved.returnFileWhenButtonClicked(filesManager.getApprovedMusicList(), mouseX, mouseY) != null) {
                try {
                    if (!player.isPaused()) {
                        player.pausePlayer();
                        pauseButton.setButtonText("\u25b6");
                        pauseButton.setButtonTextX(306);
                    }
                    player.playFile(selectMusicButtonApproved.returnFileWhenButtonClicked(filesManager.getApprovedMusicList(), mouseX, mouseY));
                } catch (JavaLayerException ex) {
                    throw new RuntimeException(ex);
                }
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
                playlistManagerScreen = false;
                folderSelectionScreen = false;

                remove(unapprovedFileSearchJTextField);
                remove(approvedFileSearchJTextField);

            } else if ( settingsDropDown.buttonClicked("FolderSelection", e.getX(), e.getY()) ) {
                //Folder Selection Button
                playerScreen = false;
                playlistManagerScreen = false;
                folderSelectionScreen = true;

                try {
                    selectedUnapprovedFolderName = googleDrive.getFileNameById(authentication.logon(), settings.getSettingValue("unapprovedplaylistfolder"));
                    selectedApprovedFolderName = googleDrive.getFileNameById(authentication.logon(), settings.getSettingValue("approvedplaylistfolder"));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                add(unapprovedFileSearchJTextField);
                add(approvedFileSearchJTextField);
            } else if (settingsDropDown.buttonClicked("MusicApproval", e.getX(), e.getY())) {
                playerScreen = false;
                playlistManagerScreen = true;
                folderSelectionScreen = false;

                try {

                    if (settings.getSettingValue("approvedplaylistfolder").equals("null") || settings.getSettingValue("unapprovedplaylistfolder").equals("null")) {
                        displayNoFolderSelectedError = true;
                        remove(unapprovedFileSearchJTextField);
                        remove(approvedFileSearchJTextField);
                    } else {
                        displayNoFolderSelectedError = false;
                        add(unapprovedFileSearchJTextField);
                        add(approvedFileSearchJTextField);
                        selectMusicButtonUnapproved = new SelectMusicButton(filesManager.getUnapprovedMusicList(), 10, 225, 206, 30, customBlue, new Color(113, 149, 255), textFont, 16, 20, 230, 10, 3, new Color(0, 217, 0), new Color(0, 162, 0), 30, 30, "\u2714", "\uD83D\uDDD1", new Color(149, 149, 149), new Color(84, 84, 84));
                        selectMusicButtonApproved = new SelectMusicButton(filesManager.getApprovedMusicList(), 364, 225, 206, 30, customBlue, new Color(133, 149, 255), textFont, 16, 376, 230, 10, 3, new Color(255, 40, 40), new Color(154, 0, 0), 30, 30, "\u274c", "\uD83D\uDDD1", new Color(149, 149, 149), new Color(84, 84, 84));
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

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