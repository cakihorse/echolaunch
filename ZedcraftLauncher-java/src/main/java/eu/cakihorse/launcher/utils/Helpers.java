package eu.cakihorse.launcher.utils;

import fr.flowarg.flowcompat.Platform;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Helpers {
    public static File generateGamePath(String FolderName) {
        Path path;
        switch(Platform.getCurrentPlatform()) {
            case WINDOWS:
                path = Paths.get(System.getenv("APPDATA"));
            case MAC:
                path = Paths.get(System.getProperty("user.home"), "/Library/Application Support/");
                break;
            case LINUX:
                path = Paths.get(System.getProperty("user.home"), ".local/share");
            default:
                path = Paths.get(System.getProperty("user.home"));

        }
        path = Paths.get(path.toString(), FolderName);

        return path.toFile();
    }
}
