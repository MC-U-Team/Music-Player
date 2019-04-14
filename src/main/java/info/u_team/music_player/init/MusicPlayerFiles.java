package info.u_team.music_player.init;

import java.io.*;
import java.nio.file.*;

import org.apache.logging.log4j.*;

import net.harawata.appdirs.*;

public class MusicPlayerFiles {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static final AppDirs appdirs = AppDirsFactory.getInstance();
	
	public static final Path directory = Paths.get(appdirs.getUserConfigDir("musicplayer", null, "mc-u-team", true));
	
	public static final Path cache = directory.resolve("cache");
	public static final Path playlist = directory.resolve("playlist");
	
	public static void construct() {
		try {
			Files.createDirectories(directory);
			Files.createDirectory(cache);
			Files.createDirectory(playlist);
		} catch (IOException ex) {
			logger.error("Could not create music player directories", ex);
		}
	}
}
