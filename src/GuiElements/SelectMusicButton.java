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
    HashMap<File, Button> moveFileButtonHashMap = new HashMap<>();
    HashMap<File, Button> deleteFileButtonHashMap = new HashMap<>();

    public SelectMusicButton(List<File> musicFiles, int x, int y, int w, int h, Color btnColor, Color btnTxtColor, Font btnFont, int btnTxtSize, int btnTxtX, int btnTxtY, int spacing, int offset, Color moveFileButtonColor, Color moveFileButtonTextColor, int actionButtonW, int actionButtonH, String moveFileButtonText, String deleteFileButtonText, Color deleteFileButtonColor, Color deleteFileButtonTextColor) {
        if (musicFiles == null) return;
        for (int i=0; i < musicFiles.size(); i++) {
            musicFileButtonHashMap.put(musicFiles.get(i), new Button(btnColor, x, y+(i*(h+spacing))+spacing, w, h, btnFont, btnTxtColor, musicFiles.get(i).getName(), btnTxtSize, btnTxtX, btnTxtY+(i*(h+spacing))+spacing*offset-offset));
            moveFileButtonHashMap.put(musicFiles.get(i), new Button(moveFileButtonColor, x+w, y+(i*(h+spacing))+spacing, actionButtonW, actionButtonH, new Font("Apple Color Emoji", Font.PLAIN, 10), moveFileButtonTextColor, moveFileButtonText, btnTxtSize, btnTxtX+w, btnTxtY+(i*(h+spacing))+spacing*offset-offset));
            deleteFileButtonHashMap.put(musicFiles.get(i), new Button(deleteFileButtonColor, x+w+actionButtonW, y+(i*(h+spacing))+spacing, actionButtonW, actionButtonH, new Font("Apple Color Emoji", Font.PLAIN, 10), deleteFileButtonTextColor, deleteFileButtonText, btnTxtSize, btnTxtX+w+actionButtonW, btnTxtY+(i*(h+spacing))+spacing*offset-offset));
        }
    }

    public void drawSelectMusicButtons(Graphics g2d, List<File> musicFiles, int mouseX, int mouseY) {
        if (musicFiles == null) return;
        for (File musicFile : musicFiles) {
            musicFileButtonHashMap.get(musicFile).drawButton(g2d, mouseX, mouseY);
            moveFileButtonHashMap.get(musicFile).drawButton(g2d, mouseX, mouseY);
            deleteFileButtonHashMap.get(musicFile).drawButton(g2d, mouseX, mouseY);
        }
    }

    public File returnFileWhenButtonClicked(List<File> musicFiles, int mouseX, int mouseY) {
        if (musicFiles == null) return null;
        for (File musicFile : musicFiles) {
            if (musicFileButtonHashMap.get(musicFile).isClicked(mouseX, mouseY)) {
                return musicFile;
            }
        }
        return null;
    }

    public File returnMoveFileButtonClicked(List<File> musicFiles, int mouseX, int mouseY) {
        if (musicFiles == null) return null;
        for (File musicFile : musicFiles) {
            if (moveFileButtonHashMap.get(musicFile).isClicked(mouseX, mouseY)) {
                return musicFile;
            }
        }
        return null;
    }

    public File returnDeleteFileButtonClicked(List<File> musicFiles, int mouseX, int mouseY) {
        if (musicFiles==null) return null;
        for (File musicFile : musicFiles) {
            if (deleteFileButtonHashMap.get(musicFile).isClicked(mouseX, mouseY)) {
                return musicFile;
            }
        }
        return null;
    }

}