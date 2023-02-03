import Window.UserInterface;
import resources.GetResources;
import resources.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class CollaborativePlaylist extends JFrame {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 700;

    public Color backgroundColor;

    private Settings settings = new Settings();

    public CollaborativePlaylist() throws GeneralSecurityException, IOException {
        super("Collaborative Playlist");

        setSize(WIDTH, HEIGHT);

        UserInterface loginScreen = new UserInterface();
        GetResources resources = new GetResources();

        loginScreen.setFocusable(true);

        setResizable(false);

        try {
            if (settings.getSettingValue("theme").equals("light")) {
                backgroundColor = new Color(255, 255, 255);
            } else if (settings.getSettingValue("theme").equals("dark")) {
                backgroundColor = new Color(0, 0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setBackground(backgroundColor);

        getContentPane().add(loginScreen);

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image icon = Toolkit.getDefaultToolkit().getImage(resources.getFileResource("icon.png"));
        setIconImage(icon);

    }

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        CollaborativePlaylist run = new CollaborativePlaylist();
    }
}
