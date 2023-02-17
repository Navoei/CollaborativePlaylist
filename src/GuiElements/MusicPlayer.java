package GuiElements;

import GoogleApi.Authentication;
import GoogleApi.FilesManager;
import GoogleApi.GoogleDrive;
import GoogleApi.SoundFile;
import com.google.api.services.drive.model.File;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import resources.Settings;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MusicPlayer {
    private final Authentication authentication = new Authentication();
    private final FilesManager filesManager = new FilesManager();
    private final GoogleDrive googleDrive = new GoogleDrive();
    private final SoundFile soundFile = new SoundFile();
    private final Settings settings = new Settings();
    private Thread playerThread;
    private Thread playSongPreviewThread;
    private static Player player;
    private boolean musicHasStarted;
    private boolean isPaused;
    private int currentTrackIndex;
    private File nowPlaying;

    public MusicPlayer() throws GeneralSecurityException, IOException {
        isPaused = false;
        musicHasStarted = false;
        currentTrackIndex = 0;
    }

    public void playPlaylist() {

        if (playSongPreviewThread!=null) {
            playSongPreviewThread.stop();
        }

        musicHasStarted = true;
        playerThread = new Thread(() -> {
            while (musicHasStarted) {
                try {
                    while (currentTrackIndex<filesManager.getApprovedMusicList().size()) {
                        filesManager.setApprovedMusicList(googleDrive.listMusicFilesInsideFolder(authentication.logon(), settings.getSettingValue("approvedplaylistfolder"), ""));
                        nowPlaying = filesManager.getApprovedMusicList().get(currentTrackIndex);
                        player = new Player(soundFile.getMusicInputStream(authentication.logon(), filesManager.getApprovedMusicList().get(currentTrackIndex)));
                        while (!isPaused && !player.isComplete()) {
                            player.play();
                        }
                        currentTrackIndex++;
                    }
                    currentTrackIndex = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        playerThread.start();
    }

    public void playFile(File file) throws JavaLayerException {

        if (playSongPreviewThread != null) {
            playSongPreviewThread.stop();
        }

        playSongPreviewThread = new Thread(() -> {
            try {
                player = new Player(soundFile.getMusicInputStream(authentication.logon(), file));
                while (!player.isComplete()) {
                    player.play();
                }
            } catch (JavaLayerException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        playSongPreviewThread.start();
    }

    public void pausePlayer() {
        isPaused = true;
        if (playerThread==null) return;
        playerThread.suspend();
    }

    public void resumePlayer() {

        if (playSongPreviewThread!=null) {
            playSongPreviewThread.stop();
        }

        playerThread.resume();
        isPaused = false;
    }

    public void toPreviousTrack() {
        if (currentTrackIndex - 1 >= 0) {
            currentTrackIndex--;
        } else {
            currentTrackIndex = filesManager.getApprovedMusicList().size() - 1;
        }
        playerThread.stop();
        playPlaylist();
    }

    public void skipTrack() {
        if (currentTrackIndex + 1 < filesManager.getApprovedMusicList().size()) {
            currentTrackIndex++;
        } else {
            currentTrackIndex = 0;
        }
        playerThread.stop();
        playPlaylist();
    }

    public boolean hasMusicStarted() {
        return musicHasStarted;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isPlaying() {
        return !player.isComplete();
    }

    public File nowPlaying() {
        return nowPlaying;
    }

}
