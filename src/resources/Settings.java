package resources;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings {
    GetResources resources = new GetResources();
    private final File settingsFile = new File("user/UserSettings.txt");

    public Settings() throws IOException {
        if (!(settingsFile.exists())) {
            settingsFile.getParentFile().mkdirs();
            settingsFile.createNewFile();
            copyInputStreamToFile(resources.getFileResourceStream("UserSettings.txt"), settingsFile);
        }
    }

    public String getSettingValue(String setting) throws FileNotFoundException {
        ArrayList<String> settings = new ArrayList<>();
        Scanner scanner = new Scanner(settingsFile);
        while (scanner.hasNextLine()) {
            settings.add(scanner.nextLine());
        }
        for (String s : settings) {
            if (s.contains(setting)) {
                return settingOption(s);
            }
        }
        return null;
    }

    public String getSetting(String setting) throws FileNotFoundException {
        ArrayList<String> settings = new ArrayList<>();
        Scanner scanner = new Scanner(settingsFile);
        while (scanner.hasNextLine()) {
            settings.add(scanner.nextLine());
        }
        for (String s : settings) {
            if (s.contains(setting)) {
                return s;
            }
        }
        return null;
    }

    public void setSetting(String setting, String newValue) throws IOException {
        Path filePath = Paths.get(settingsFile.toURI());
        String textFile = Files.readString(filePath, Charset.defaultCharset());

        textFile = textFile.replace(getSetting(setting), getSetting(setting).replace(getSettingValue(setting), newValue));
        Files.writeString(filePath, textFile, Charset.defaultCharset());
    }

    public String settingOption(String setting) {
        Pattern p = Pattern.compile(".*'([^']*)'.*");
        Matcher matcher = p.matcher(setting);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static void copyInputStreamToFile(InputStream input, File file) {

        try (OutputStream output = new FileOutputStream(file)) {
            input.transferTo(output);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

}
