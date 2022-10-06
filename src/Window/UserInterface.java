package Window;

import GuiElements.Images;
import GuiElements.Button;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import resources.GetResources;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class UserInterface extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    private BufferedImage back;
    private ImageIcon logo, settingsIcon;
    private Button button;
    private static final GetResources resources = new GetResources();

    private int mouseX, mouseY;

    private boolean loginScreen, playerScreen;
    private boolean drawConnectionError = false;
    private boolean browserOpen = false;

    private Credential USER_CREDENTIALS;

    private Font textFont = new Font("Berlin Sans FB", Font.PLAIN, 36);

    private static final String APPLICATION_NAME = "InteractivePlaylist";
    private static final String TOKENS_DIRECTORY_PATH = resources.getFileResource("credentials.json").getPath().replace("credentials.json", "user");
    private static final String CREDENTIALS_FILE_PATH = resources.getFileResource("credentials.json").getFile().replaceFirst("/", "");

    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE);

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

    //private JTextField usernameJTextField;
    //private JPasswordField passwordJTextField;

    public UserInterface() throws GeneralSecurityException, IOException {

        new Thread(this).start();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        loginScreen = true;
        playerScreen = false;

        Images images = new Images();
        button = new Button();

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

    private void drawFeedbackText(Graphics g2d) {
        if (drawConnectionError) {
            g2d.setColor(new Color(255,0,0));
            g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 24));
            g2d.drawString("No internet connection.", 200,400);
        }
    }

    public void openBrowser(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        if (browserOpen) {
            InputStream in = resources.getClass().getResourceAsStream("credentials.json");
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
            }
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            USER_CREDENTIALS = new AuthorizationCodeInstalledApp(flow, receiver).authorize("InteractivePlaylistClient");

            System.out.println("Access Token: " + USER_CREDENTIALS.getAccessToken());

            //If authentication successful get rid of login screen
            loginScreen = false;
            browserOpen = false;
            playerScreen = true;

            printFilesInDrive();

        }

    }

    private void printFilesInDrive() throws IOException {
        // Build a new authorized API client service.
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, USER_CREDENTIALS)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Print the names and IDs for up to 10 folders.
        //https://developers.google.com/drive/api/guides/search-files
        FileList result = service.files().list()
                .setQ("mimeType = 'application/vnd.google-apps.folder'")
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Folders:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }

    private void signOut() {
        java.io.File file = new java.io.File(TOKENS_DIRECTORY_PATH + "/StoredCredential");
        if (file.exists()) {
            file.delete();
            loginScreen = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if ( loginScreen && (((e.getX() > 243) && (e.getX() < 393)) && ((e.getY() > 320) && (e.getY() < 360))) ) {
            if (checkInternetConnection()) {
                drawConnectionError = false;
                browserOpen = true;
                try {
                    openBrowser(HTTP_TRANSPORT);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                drawConnectionError = true;
            }
        }
        //button.drawButton(g2d, new Color(0, 71, 255), 595, 5, 30, 30, new Font("Berlin Sans FB", Font.PLAIN, 20), new Color(113, 149, 255), "", 30, 281, 348, mouseX, mouseY);
        if (!loginScreen && (((e.getX() > 595) && (e.getX() < 625)) && ((e.getY() > 5) && (e.getY() < 35))) ) {
            signOut();
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