package info.u_team.music_player.musicplayer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MusicPlayerManager {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static IMusicPlayer PLAYER;
	
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	private static final PlaylistManager PLAYLIST_MANAGER = new PlaylistManager(GSON);
	private static final SettingsManager SETTINGS_MANAGER = new SettingsManager(GSON);
	
	private static void setup(FMLClientSetupEvent event) {
		generatePlayer();
		PLAYER.startAudioOutput();
		PLAYLIST_MANAGER.loadFromFile();
		SETTINGS_MANAGER.loadFromFile();
		
		PLAYER.setVolume(SETTINGS_MANAGER.getSettings().getVolume());
	}
	
	private static void generatePlayer() {
		try {
			final Class<?> clazz = Class.forName("info.u_team.music_player.lavaplayer.MusicPlayer", true, DependencyManager.MUSICPLAYER_CLASSLOADER);
			if (!IMusicPlayer.class.isAssignableFrom(clazz)) {
				throw new IllegalAccessError("The class " + clazz + " does not implement IMusicPlayer! This should not happen?!");
			}
			PLAYER = (IMusicPlayer) clazz.getDeclaredConstructor().newInstance();
			LOGGER.info("Successfully created music player instance");
		} catch (final Exception ex) {
			LOGGER.fatal("Cannot create music player instance. This is a serious bug and the mod will not work. Report to the mod authors", ex);
			System.exit(-1);
		}
	}
	
	public static IMusicPlayer getPlayer() {
		return PLAYER;
	}
	
	public static PlaylistManager getPlaylistManager() {
		return PLAYLIST_MANAGER;
	}
	
	public static SettingsManager getSettingsManager() {
		return SETTINGS_MANAGER;
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(MusicPlayerManager::setup);
	}
}
