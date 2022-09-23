import Screen.LoginScreen;

import javax.swing.*;
import java.awt.*;

public class CollaborativePlaylist extends JFrame {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 700;

    public CollaborativePlaylist() {
        super("Collaborative Playlist");

        setSize(WIDTH, HEIGHT);

        LoginScreen loginScreen = new LoginScreen();

        loginScreen.setFocusable(true);

        Color backgroundColor = new Color(255, 255, 255);

        setBackground(backgroundColor);

        getContentPane().add(loginScreen);

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Image icon = Toolkit.getDefaultToolkit().getImage("interactiveplaylisticon.png");
        //setIconImage(icon);
    }

    public static void main(String[] args) {
        CollaborativePlaylist run = new CollaborativePlaylist();
    }
}
