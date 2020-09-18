package info.u_team.music_player.init;

import java.nio.file.*;

import org.apache.logging.log4j.*;

import info.u_team.music_player.config.ClientConfig;
import net.harawata.appdirs.AppDirsFactory;
import net.minecraft.client.Minecraft;

public class MusicPlayerFiles {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static Path DIRECTORY;
	
	public static void load() {
		if (ClientConfig.getInstance().internalPlaylists.get()) {
			DIRECTORY = Paths.get(Minecraft.getInstance().gameDir.toString(), "config/musicplayer");
		} else {
			DIRECTORY = Paths.get(AppDirsFactory.getInstance().getUserConfigDir("musicplayer", null, "mc-u-team", true));
		}
		
		try {
			Files.createDirectories(DIRECTORY);
		} catch (final Exception ex) {
			LOGGER.error("Could not create music player directories", ex);
		}
	}
	
	public static Path getDirectory() {
		return DIRECTORY;
	}
}
