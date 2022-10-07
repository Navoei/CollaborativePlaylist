package GuiElements;

import resources.Settings;

import java.awt.*;
import java.io.FileNotFoundException;

public class SettingsDropDown {

    Button button;
    Settings settings;

    private Font buttonFont;

    public SettingsDropDown() throws FileNotFoundException {
        button = new Button();
        settings = new Settings();

        buttonFont = new Font("Berlin Sans FB", Font.PLAIN, 15);
    }

    public void drawSettingsDropDown(Graphics g2d, Color menuBackgroundColor, int mouseX, int mouseY) throws FileNotFoundException {
        g2d.setColor(menuBackgroundColor);
        g2d.fillRect(425,5,200,300);
        g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 20));
        g2d.setColor(new Color(255,255,255));
        g2d.drawString("Settings" , 445, 25);
        button.drawButton(g2d,  new Color(113, 149, 255), 435, 35, 150, 25, buttonFont, new Color(255,255,255), "Theme: " + settings.getSettingValue("theme"), 20, 445, 53, mouseX, mouseY);
    }

}
