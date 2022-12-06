package GuiElements;

import com.google.api.services.drive.model.File;

import java.awt.*;
import java.util.List;

public class SelectFolderButton {
    Button button;
    int x;
    int y;
    int w;
    int h;

    public SelectFolderButton() {
        button = new Button();
    }

    public void drawSelectFolderButton(Graphics g2d, List<File> unapprovedFiles, int x, int y, int w, int h, Color btnColor, Color btnTxtColor, Font btnFont, int btnTxtSize, int btnTxtX, int btnTxtY, int mouseX, int mouseY, int spacing, int offset) {

        for (int i = 0; i < unapprovedFiles.size() ; i++) {
            button.drawButton(g2d, btnColor, x, y+(i*(h+spacing))+spacing, w, h, btnFont, btnTxtColor, unapprovedFiles.get(i).getName(), btnTxtSize, btnTxtX, btnTxtY+(i*(h+spacing))+spacing*offset-offset, mouseX, mouseY);
        }

    }

}
