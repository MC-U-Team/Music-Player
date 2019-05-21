package info.u_team.music_player.musicplayer;

import org.apache.logging.log4j.*;

import com.google.gson.*;

import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class MusicPlayerManager {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static IMusicPlayer player;
	
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private static final PlaylistManager playlistManager = new PlaylistManager(gson);
	private static final SettingsManager settingsManager = new SettingsManager(gson);
	
	public static void setup() {
		generatePlayer();
		player.startAudioOutput();
		playlistManager.loadFromFile();
		settingsManager.loadFromFile();
		
		player.setVolume(settingsManager.getSettings().getVolume());
	}
	
	private static void generatePlayer() {
		try {
			Class<?> clazz = Class.forName("info.u_team.music_player.lavaplayer.MusicPlayer", true, DependencyManager.getClassLoader());
			if (!IMusicPlayer.class.isAssignableFrom(clazz)) {
				throw new IllegalAccessError("The class " + clazz + " does not implement IMusicPlayer! This should not happen?!");
			}
			player = (IMusicPlayer) clazz.newInstance();
			logger.info("Successfully created music player instance");
		} catch (Exception ex) {
			logger.fatal("Cannot create music player instance. This is a serious bug and the mod will not work. Report to the mod authors", ex);
			FMLCommonHandler.instance().exitJava(0, false);
		}
	}
	
	public static IMusicPlayer getPlayer() {
		return player;
	}
	
	public static PlaylistManager getPlaylistManager() {
		return playlistManager;
	}
	
	public static SettingsManager getSettingsManager() {
		return settingsManager;
	}
}
