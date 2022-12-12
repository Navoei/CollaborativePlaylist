package GoogleApi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleDrive {

    private final Authentication authentication = new Authentication();

    public GoogleDrive() throws GeneralSecurityException, IOException {
    }

    public List<File> listFolders(Credential userCredentials, String fileName) throws IOException {
        // Build a new authorized API client service.
        com.google.api.services.drive.Drive service = new com.google.api.services.drive.Drive.Builder(authentication.HTTP_TRANSPORT, authentication.getJsonFactory(), userCredentials)
                .setApplicationName(authentication.getApplicationName())
                .build();

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setQ("mimeType = 'application/vnd.google-apps.folder' and name contains '" + fileName + "'")
                .setPageSize(9)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
            return null;
        } else {
            System.out.println("Folders:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
        return files;
    }
}
