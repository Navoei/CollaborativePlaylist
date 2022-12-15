package GoogleApi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import resources.GetResources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class Authentication {

    private static final GetResources resources = new GetResources();

    private static final String APPLICATION_NAME = "InteractivePlaylist";

    private static final String TOKENS_DIRECTORY_PATH = resources.getFileResource("credentials.json").getPath().replace("credentials.json", "user");
    private static final String CREDENTIALS_FILE_PATH = resources.getFileResource("credentials.json").getFile().replaceFirst("/", "");

    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE);

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

    public Authentication() throws GeneralSecurityException, IOException {
    }

    public Credential logon() throws IOException {
        InputStream in = resources.getClass().getResourceAsStream("credentials.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("InteractivePlaylistClient");
    }

    public void logout() {
        java.io.File file = new java.io.File(TOKENS_DIRECTORY_PATH + "/StoredCredential");
        if (file.exists()) {
            file.delete();
        }
    }

    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    public JsonFactory getJsonFactory() {
        return JSON_FACTORY;
    }

}
