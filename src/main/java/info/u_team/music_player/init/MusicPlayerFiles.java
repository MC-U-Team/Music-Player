package info.u_team.music_player.init;

import java.nio.file.*;

import org.apache.logging.log4j.*;

import net.harawata.appdirs.AppDirsFactory;

public class MusicPlayerFiles {

	private static final Logger logger = LogManager.getLogger();

	public static final Path directory = Paths.get(AppDirsFactory.getInstance().getUserConfigDir("musicplayer", null, "mc-u-team", true));

	public static void construct() {
		try {
			Files.createDirectories(directory);
		} catch (Exception ex) {
			logger.error("Could not create music player directories", ex);
		}
	}
}
