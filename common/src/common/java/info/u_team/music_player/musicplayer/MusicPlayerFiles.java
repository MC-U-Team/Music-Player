package info.u_team.music_player.musicplayer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.harawata.appdirs.AppDirsFactory;
import net.minecraft.client.Minecraft;

public class MusicPlayerFiles {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private Path directory;
	
	MusicPlayerFiles() {
	}
	
	public void load(boolean internalPlaylists) {
		if (internalPlaylists) {
			directory = Paths.get(Minecraft.getInstance().gameDirectory.toString(), "config/musicplayer");
		} else {
			directory = Paths.get(AppDirsFactory.getInstance().getUserConfigDir("musicplayer", null, "mc-u-team", true));
		}
		
		try {
			Files.createDirectories(directory);
		} catch (final Exception ex) {
			LOGGER.error("Could not create music player directories", ex);
		}
	}
	
	public Path getDirectory() {
		return directory;
	}
}
