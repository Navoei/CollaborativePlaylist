package GuiElements;

import resources.Settings;

import java.awt.*;
import java.util.HashMap;

public class SettingsDropDown {

    HashMap<String, Button> settingsButtonHashMap = new HashMap<>();
    Settings settings;

    private Font buttonFont = new Font("Berlin Sans FB", Font.PLAIN, 16);
    private int width;
    private int height;
    private int x;
    private int y;
    private Color menuBackgroundColor;

    public SettingsDropDown() {
        settings = new Settings();
        width = 0;
        height = 0;
        x = 0;
        y = 0;
    }

    public SettingsDropDown(Color menuBackgroundColor, int x, int y, int w, int h, String settingsValue) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        this.menuBackgroundColor = menuBackgroundColor;
        settingsButtonHashMap.put("Theme", new Button(new Color(113, 149, 255), 435, 35, 150, 25, buttonFont, new Color(255,255,255), "Theme: " + settingsValue, 20, 445, 53));
        settingsButtonHashMap.put("MusicPlayer", new Button(new Color(113, 149, 255), 435, 65, 150, 25, buttonFont, new Color(255,255,255), "Music Player", 20, 445, 83));
        settingsButtonHashMap.put("FolderSelection", new Button(new Color(113, 149, 255), 435, 95, 150, 25, buttonFont, new Color(255,255,255), "Folder Selection", 20, 445, 113));
    }

    public void drawSettingsDropDown(Graphics g2d, int mouseX, int mouseY) {
        g2d.setColor(menuBackgroundColor);
        g2d.fillRect(x,y,width,height);
        g2d.setFont(new Font("Berlin Sans FB", Font.PLAIN, 20));
        g2d.setColor(new Color(255,255,255));
        g2d.drawString("Settings" , 445, 25);
        settingsButtonHashMap.get("Theme").drawButton(g2d, mouseX, mouseY);
        settingsButtonHashMap.get("MusicPlayer").drawButton(g2d, mouseX, mouseY);
        settingsButtonHashMap.get("FolderSelection").drawButton(g2d, mouseX, mouseY);
    }

    public boolean buttonClicked(String buttonName, int mouseXV, int mouseYV) {
        return settingsButtonHashMap.get(buttonName).isClicked(mouseXV, mouseYV);
    }

    public void setThemeText(String text) {
        settingsButtonHashMap.get("Theme").setButtonText("Theme: " + text);
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
