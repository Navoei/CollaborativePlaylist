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

        // Print the names and IDs for up to 9 files.
        FileList result = service.files().list()
                .setQ("mimeType = 'application/vnd.google-apps.folder' and name contains '" + fileName + "' and trashed=false")
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

    public List<File> listMusicFilesInsideFolder(Credential userCredentials, String folderId, String fileName) throws IOException {

        com.google.api.services.drive.Drive service = new com.google.api.services.drive.Drive.Builder(authentication.HTTP_TRANSPORT, authentication.getJsonFactory(), userCredentials)
                .setApplicationName(authentication.getApplicationName())
                .build();

        FileList result = service.files().list()
                .setQ("'"+folderId+"' in parents and trashed=false and (name contains '" + fileName + ".wav' or name contains '" + fileName + ".mp3')")
                .setPageSize(9)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
            return null;
        } else {
            System.out.println("Music Files:");
            for (File file : files) {
                System.out.printf("%s (%S)\n", file.getName(), file.getId());
            }
        }
        return files;
    }

    public String getFileNameById(Credential userCredentials, String id) {

        com.google.api.services.drive.Drive service = new com.google.api.services.drive.Drive.Builder(authentication.HTTP_TRANSPORT, authentication.getJsonFactory(), userCredentials)
                .setApplicationName(authentication.getApplicationName())
                .build();

        try {
            File file = service.files().get(id).execute();
            return file.getName();
        } catch (Exception e) {
            System.out.println("An Error Occurred!: " + e);
        }
        return null;
    }

}