import Login.LoginScreen;

import javax.swing.*;
import java.awt.*;

public class InteractivePlaylist extends JFrame {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 700;

    public InteractivePlaylist() {
        super("Interactive Playlist");
        setSize(WIDTH, HEIGHT);

        LoginScreen loginScreen = new LoginScreen();

        ((Component) loginScreen).setFocusable(true);

        Color white = new Color(255, 255, 255);
        Color black = new Color(0,0,0);

        setBackground(white);

        getContentPane().add(loginScreen);

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Image icon = Toolkit.getDefaultToolkit().getImage("interactiveplaylisticon.png");
        //setIconImage(icon);
    }

    public static void main(String[] args) {
        InteractivePlaylist run = new InteractivePlaylist();
    }
}
