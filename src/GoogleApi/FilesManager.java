package GoogleApi;

import com.google.api.services.drive.model.File;

import java.util.ArrayList;
import java.util.List;

public class FilesManager {

    private static List<File> unapprovedFoldersList = new ArrayList<>();
    private static List<File> approvedFoldersList = new ArrayList<>();
    private static List<File> unapprovedMusicList = new ArrayList<>();
    private static List<File> approvedMusicList = new ArrayList<>();

    public void setUnapprovedFoldersList(List<File> folderList) {
        unapprovedFoldersList = folderList;
    }

    public void setApprovedFoldersList(List<File> folderList) {
        approvedFoldersList = folderList;
    }

    public void setUnapprovedMusicList(List<File> folderList) {
        unapprovedMusicList = folderList;
    }

    public void setApprovedMusicList(List<File> folderList) {
        approvedMusicList = folderList;
    }

    public List<File> getUnapprovedFoldersList() {
        return unapprovedFoldersList;
    }

    public List<File> getApprovedFoldersList() {
        return approvedFoldersList;
    }

    public List<File> getUnapprovedMusicList() {
        return unapprovedMusicList;
    }

    public List<File> getApprovedMusicList() {
        return approvedMusicList;
    }

}
