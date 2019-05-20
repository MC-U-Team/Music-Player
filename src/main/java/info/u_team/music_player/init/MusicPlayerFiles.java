package info.u_team.music_player.init;

import java.nio.file.*;

import org.apache.logging.log4j.*;

import info.u_team.music_player.config.ClientConfig;
import net.harawata.appdirs.AppDirsFactory;
import net.minecraft.client.Minecraft;

public class MusicPlayerFiles {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static Path directory;
	
	public static void setup() {
		if (ClientConfig.client.internalPlaylists) {
			directory = Paths.get(Minecraft.getMinecraft().gameDir.toString(), "config/musicplayer");
		} else {
			directory = Paths.get(AppDirsFactory.getInstance().getUserConfigDir("musicplayer", null, "mc-u-team", true));
		}
		
		try {
			Files.createDirectories(directory);
		} catch (Exception ex) {
			logger.error("Could not create music player directories", ex);
		}
	}
	
	public static Path getDirectory() {
		return directory;
	}
}
