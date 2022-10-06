import Window.UserInterface;
import resources.GetResources;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class CollaborativePlaylist extends JFrame {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 700;

    public CollaborativePlaylist() throws GeneralSecurityException, IOException {
        super("Collaborative Playlist");

        setSize(WIDTH, HEIGHT);

        UserInterface loginScreen = new UserInterface();
        GetResources resources = new GetResources();

        loginScreen.setFocusable(true);

        setResizable(false);

        Color backgroundColor = new Color(255, 255, 255);

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
