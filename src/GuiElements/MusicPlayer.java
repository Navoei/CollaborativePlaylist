package GuiElements;

import GoogleApi.SoundFile;
import Window.UserInterface;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;
import javazoom.jl.player.Player;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class MusicPlayer {

    SoundFile soundFile = new SoundFile();
    private Player player;
    private Thread playerThread;
    private int currentTrack = 0;
    private File nowPlaying;
    public boolean isPaused = false;
    public boolean musicHasStarted = false;

    public MusicPlayer() throws GeneralSecurityException, IOException {
    }


    public void playApprovedPlaylist(Credential credentials, List<File> musicFiles) throws IOException {
        musicHasStarted = true;
        playerThread = new Thread(() -> {
            while (musicHasStarted) {
                try {
                    while (currentTrack<musicFiles.size()) {
                        player = new Player(soundFile.getAudioInputStream(credentials, musicFiles.get(currentTrack)));
                        nowPlaying = musicFiles.get(currentTrack);
                        while (!isPaused && !player.isComplete()) {
                            player.play();
                        }
                        currentTrack++;
                    }
                    currentTrack = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        playerThread.start();
    }

    public void skip(Credential credentials, List<File> musicFiles) throws IOException {
        if (currentTrack + 1 < musicFiles.size()) {
            currentTrack++;
        } else {
            currentTrack = 0;
        }
        playerThread.stop();
        playApprovedPlaylist(credentials, musicFiles);
    }

    public void previous(Credential credentials, List<File> musicFiles) throws IOException {
        if (currentTrack - 1 >= 0) {
            currentTrack--;
        } else {
            currentTrack = musicFiles.size() - 1;
        }
        playerThread.stop();
        playApprovedPlaylist(credentials, musicFiles);
    }

    public void pause() {
        playerThread.suspend();
        isPaused = true;
    }

    public void resume() {
        playerThread.resume();
        isPaused = false;
    }

    public File getCurrentSong() {
        return nowPlaying;
    }

}
