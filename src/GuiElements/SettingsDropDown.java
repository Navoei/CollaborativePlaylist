package GuiElements;

import resources.Settings;

import java.awt.*;
import java.io.FileNotFoundException;

public class SettingsDropDown {

    Button button;
    Settings settings;

    private Font buttonFont;
    private int width;
    private int height;
    private int x;
    private int y;

    public SettingsDropDown() {
        button = new Button();
        settings = new Settings();

        buttonFont = new Font("Berlin Sans FB", Font.PLAIN, 15);
    }

    public void drawSettingsDropDown(Graphics g2d, Color menuBackgroundColor, int x, int y, int w, int h, int mouseX, int mouseY) throws FileNotFoundException {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        g2d.setColor(menuBackgroundColor);
        g2d.fillRect(x,y,w,h);
        g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 20));
        g2d.setColor(new Color(255,255,255));
        g2d.drawString("Settings" , 445, 25);
        button.drawButton(g2d,  new Color(113, 149, 255), 435, 35, 150, 25, buttonFont, new Color(255,255,255), "Theme: " + settings.getSettingValue("theme"), 20, 445, 53, mouseX, mouseY);
        button.drawButton(g2d,  new Color(113, 149, 255), 435, 65, 150, 25, buttonFont, new Color(255,255,255), "Music Player", 20, 445, 83, mouseX, mouseY);
        button.drawButton(g2d,  new Color(113, 149, 255), 435, 95, 150, 25, buttonFont, new Color(255,255,255), "Folder Selection", 20, 445, 113, mouseX, mouseY);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
