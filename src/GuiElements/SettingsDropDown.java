package GuiElements;

import java.awt.*;

public class SettingsDropDown {

    Button button = new Button();

    public void drawSettingsDropDown(Graphics g2d, Color menuBackgroundColor, int mouseX, int mouseY) {
        g2d.setColor(menuBackgroundColor);
        g2d.fillRect(425,5,200,300);
        g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 20));
        g2d.setColor(new Color(255,255,255));
        g2d.drawString("Settings" , 445, 25);
        button.drawButton(g2d,  new Color(113, 149, 255), 435, 30, 150, 25, new Font("Berlin Sans FB", Font.PLAIN, 15), new Color(255,255,255), "Theme: ", 20, 445, 48, mouseX, mouseY);
    }

}
