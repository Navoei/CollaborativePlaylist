package GoogleApi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public class SoundFile {

    private final Authentication authentication = new Authentication();

    public SoundFile() throws GeneralSecurityException, IOException {
    }

    public InputStream getMusicInputStream(Credential userCredentials, File musicFile) throws IOException {
        com.google.api.services.drive.Drive service = new com.google.api.services.drive.Drive.Builder(authentication.HTTP_TRANSPORT, authentication.getJsonFactory(), userCredentials)
                .setApplicationName(authentication.getApplicationName())
                .build();

        return service.files().get(musicFile.getId()).executeMediaAsInputStream();

    }

}
