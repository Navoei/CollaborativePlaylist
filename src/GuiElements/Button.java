package GuiElements;

import javax.swing.*;
import java.awt.*;

public class Button extends JPanel {

    public Button() {
    }

    public void drawButton(Graphics g2d, Color btnColor, int btnX, int btnY, int btnW, int btnH, Font buttonTextFont, Color btnTextColor, String btnText, int btnTextSize, int btnTextX, int btnTextY, int mouseX, int mouseY) {
        if ( (((mouseX > btnX) && (mouseX < btnX+btnW)) && ((mouseY > btnY) && (mouseY < btnY+btnH))) ) {
            g2d.setColor(btnTextColor);
            g2d.fillRect(btnX, btnY, btnW, btnH);
            g2d.setFont(new Font(buttonTextFont.getName(), buttonTextFont.getStyle(), btnTextSize));
            g2d.setColor(btnColor);
            g2d.drawString(btnText, btnTextX, btnTextY);
        } else {
            g2d.setColor(btnColor);
            g2d.fillRect(btnX, btnY, btnW, btnH);
            g2d.setFont(new Font(buttonTextFont.getName(), buttonTextFont.getStyle(), btnTextSize));
            g2d.setColor(btnTextColor);
            g2d.drawString(btnText, btnTextX, btnTextY);
        }
    }
}
