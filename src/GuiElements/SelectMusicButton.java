package GuiElements;

import GoogleApi.GoogleDrive;
import com.google.api.services.drive.model.File;

import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

public class SelectMusicButton {

    HashMap<File, Button> musicFileButtonHashMap = new HashMap<>();
    GoogleDrive googleDrive;

    public SelectMusicButton() throws GeneralSecurityException, IOException {
        googleDrive = new GoogleDrive();
    }

    public SelectMusicButton(List<File> musicFiles, int x, int y, int w, int h, Color btnColor, Color btnTxtColor, Font btnFont, int btnTxtSize, int btnTxtX, int btnTxtY, int spacing, int offset) {
        if (musicFiles == null) return;
        for (int i=0; i < musicFiles.size(); i++) {
            musicFileButtonHashMap.put(musicFiles.get(i), new Button(btnColor, x, y+(i*(h+spacing))+spacing, w, h, btnFont, btnTxtColor, musicFiles.get(i).getName(), btnTxtSize, btnTxtX, btnTxtY+(i*(h+spacing))+spacing*offset-offset));
        }
    }

    public void drawSelectMusicButttons(Graphics g2d, List<File> musicFiles, int mouseX, int mouseY) {
        if (musicFiles == null) return;
        for (File musicFile : musicFiles) {
            musicFileButtonHashMap.get(musicFile).drawButton(g2d, mouseX, mouseY);
        }
    }

    public File returnButtonClicked(List<File> musicFiles, int mouseX, int mouseY) {
        if (musicFiles == null) return null;
        for (File musicFile : musicFiles) {
            if (musicFileButtonHashMap.get(musicFile).isClicked(mouseX, mouseY)) {
                return musicFile;
            }
        }
        return null;
    }

}
