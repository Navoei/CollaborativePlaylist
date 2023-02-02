package GuiElements;

import GoogleApi.SoundFile;
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
                    for (File music : musicFiles) {
                        player = new Player(soundFile.getAudioInputStream(credentials, music));
                        nowPlaying = music;
                        while (!isPaused && !player.isComplete()) {
                            player.play();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        playerThread.start();
    }


    /*
    Get the list of music files. Send it to a method where it begins playing the first song. Set it to be skippable and rewindable.
     */


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
