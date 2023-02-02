package GoogleApi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public class PlaySoundFile {

    Player player;

    private final Authentication authentication = new Authentication();

    public PlaySoundFile() throws GeneralSecurityException, IOException {
    }

    public void playMusic(Credential userCredentials, File musicFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException, JavaLayerException {
        com.google.api.services.drive.Drive service = new com.google.api.services.drive.Drive.Builder(authentication.HTTP_TRANSPORT, authentication.getJsonFactory(), userCredentials)
                .setApplicationName(authentication.getApplicationName())
                .build();


        new Thread(() -> {
            try {
                InputStream inputStream = service.files().get(musicFile.getId()).executeMediaAsInputStream();
                player = new Player(inputStream);
                player.play();
            } catch (JavaLayerException | IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

}
