package GuiElements;

import javax.swing.*;
import java.awt.*;

public class Button extends JPanel {

    public int buttonX, buttonY, buttonW, buttonH, buttonTextSize, buttonTextX, buttonTextY, mouseXV, mouseYV;
    public Color buttonColor, buttonTextColor;
    public Font buttonTextFont;
    public String buttonText;

    public Button() {
        buttonX = 0;
        buttonY = 0;
        buttonW = 0;
        buttonH = 0;
        buttonTextSize = 0;
        buttonTextX = 0;
        buttonTextY = 0;
        mouseXV = 0;
        mouseYV = 0;
        buttonColor = Color.BLUE;
        buttonTextColor = Color.WHITE;
        buttonTextFont = new Font("Impact", Font.BOLD, 10);
        buttonText = "Default Text";
    }

    public Button(Color btnColor, int btnX, int btnY, int btnW, int btnH, Font buttonTextFont, Color btnTextColor, String btnText, int btnTextSize, int btnTextX, int btnTextY) {
        buttonColor = btnColor;
        buttonTextColor = btnTextColor;
        buttonX = btnX;
        buttonY = btnY;
        buttonW = btnW;
        buttonH = btnH;
        this.buttonTextFont = buttonTextFont;
        buttonText = btnText;
        buttonTextSize = btnTextSize;
        buttonTextX = btnTextX;
        buttonTextY = btnTextY;
    }

    public void drawButton(Graphics g2d, int mouseXV, int mouseYV) {
        if ( (((mouseXV > getButtonX()) && (mouseXV < getButtonX()+getButtonW())) && ((mouseYV > getButtonY()) && (mouseYV < getButtonY()+getButtonH()))) ) {
            g2d.setColor(getButtonTextColor());
            g2d.fillRect(getButtonX(), getButtonY(), getButtonW(), getButtonH());
            g2d.setFont(new Font(buttonTextFont.getName(), buttonTextFont.getStyle(), getButtonTextSize()));
            g2d.setColor(getButtonColor());
            if (getButtonText().length() > 24) {
                g2d.drawString(getButtonText().substring(0, 24) + "...", getButtonTextX(), getButtonTextY());
            } else {
                g2d.drawString(getButtonText(), getButtonTextX(), getButtonTextY());
            }

        } else {
            g2d.setColor(getButtonColor());
            g2d.fillRect(getButtonX(), getButtonY(), getButtonW(), getButtonH());
            g2d.setFont(new Font(buttonTextFont.getName(), buttonTextFont.getStyle(), getButtonTextSize()));
            g2d.setColor(getButtonTextColor());
            if (getButtonText().length() > 24) {
                g2d.drawString(getButtonText().substring(0, 24) + "...", getButtonTextX(), getButtonTextY());
            } else {
                g2d.drawString(getButtonText(), getButtonTextX(), getButtonTextY());
            }
        }
    }

    public boolean isClicked(int mouseXV, int mouseYV) {
        return ((mouseXV > getButtonX()) && (mouseXV < getButtonX() + getButtonW())) && ((mouseYV > getButtonY()) && (mouseYV < getButtonY() + getButtonH()));
    }

    public boolean isMouseHovering(int mouseXV, int mouseYV) {
        return ((mouseXV > getButtonX()) && (mouseXV < getButtonX() + getButtonW())) && ((mouseYV > getButtonY()) && (mouseYV < getButtonY() + getButtonH()));
    }

    public int getButtonX() {
        return buttonX;
    }

    public int getButtonY() {
        return buttonY;
    }

    public int getButtonW() {
        return buttonW;
    }

    public int getButtonH() {
        return buttonH;
    }

    public int getButtonTextSize() {
        return buttonTextSize;
    }

    public int getButtonTextX() {
        return buttonTextX;
    }

    public int getButtonTextY() {
        return buttonTextY;
    }

    public int getMouseXV() {
        return mouseXV;
    }

    public int getMouseYV() {
        return mouseYV;
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    public Color getButtonTextColor() {
        return buttonTextColor;
    }

    public Font getButtonTextFont() {
        return buttonTextFont;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String newButtonText) {
        buttonText = newButtonText;
    }
}
