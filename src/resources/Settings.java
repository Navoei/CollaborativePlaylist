package resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings {

    GetResources resources = new GetResources();
    private final File settingsFile = new File(resources.getFileResource("credentials.json").getPath().replace("credentials.json", "user/UserSettings.txt"));

    public String getSettings(String setting) throws FileNotFoundException {
        Scanner scanner = new Scanner(settingsFile);
        ArrayList<String> settings = new ArrayList<>();
        while (scanner.hasNextLine()) {
            settings.add(scanner.nextLine());
        }
        for (String s : settings) {
            if (s.contains(setting)) {
                Pattern p = Pattern.compile(".*'([^']*)'.*");
                Matcher matcher = p.matcher(s);
                if (matcher.matches()) {
                    return matcher.group(1);
                }
            }
        }
        return null;
    }

}
