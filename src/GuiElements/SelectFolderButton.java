package GuiElements;

import GoogleApi.GoogleDrive;
import com.google.api.services.drive.model.File;

import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;


public class SelectFolderButton {
    HashMap<File, Button> fileButtonHashMap = new HashMap<>();
    GoogleDrive googleDrive;

    public SelectFolderButton() throws GeneralSecurityException, IOException {
        googleDrive = new GoogleDrive();
    }

    public SelectFolderButton(List<File> files, int x, int y, int w, int h, Color btnColor, Color btnTxtColor, Font btnFont, int btnTxtSize, int btnTxtX, int btnTxtY, int mouseX, int mouseY, int spacing, int offset) {
        if (files == null) return;
        for (int i=0; i < files.size(); i++) {
            fileButtonHashMap.put(files.get(i), new Button(btnColor, x, y+(i*(h+spacing))+spacing, w, h, btnFont, btnTxtColor, files.get(i).getName(), btnTxtSize, btnTxtX, btnTxtY+(i*(h+spacing))+spacing*offset-offset));
        }
    }

    public void drawSelectFolderButtons(Graphics g2d, List<File> files, int mouseX, int mouseY) {

        if (files==null) return;

        for (File file : files) {
            fileButtonHashMap.get(file).drawButton(g2d, mouseX, mouseY);
        }

    }

    public File returnButtonClicked(List<File> files, int mouseX, int mouseY) {
        if (files == null) return null;
        for (File file : files) {
            if (fileButtonHashMap.get(file).isClicked(mouseX, mouseY)) {
                return file;
            }
        }
        return null;
    }

}
