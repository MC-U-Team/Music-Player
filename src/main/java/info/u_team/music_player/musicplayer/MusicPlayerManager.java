package info.u_team.music_player.musicplayer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MusicPlayerManager {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static IMusicPlayer player;
	
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private static final MusicPlayerFiles files = new MusicPlayerFiles();
	
	private static final PlaylistManager playListManager = new PlaylistManager(gson);
	private static final SettingsManager settingsManager = new SettingsManager(gson);
	
	private static void setup(FMLClientSetupEvent event) {
		fixLogLevel();
		generatePlayer();
		player.startAudioOutput();
		
		files.load();
		
		playListManager.setBasePath(files.getDirectory());
		settingsManager.setBasePath(files.getDirectory());
		
		playListManager.loadFromFile();
		settingsManager.loadFromFile();
		
		player.setVolume(settingsManager.getSettings().getVolume());
	}
	
	private static void fixLogLevel() {
		final String loggerName = "org.apache.http";
		final LoggerContext context = ((LoggerContext) LogManager.getContext(DependencyManager.MUSICPLAYER_CLASSLOADER, false));
		context.getConfiguration().addLogger(loggerName, new LoggerConfig(loggerName, Level.INFO, true));
	}
	
	private static void generatePlayer() {
		try {
			final Class<?> clazz = Class.forName("info.u_team.music_player.lavaplayer.MusicPlayer", true, DependencyManager.MUSICPLAYER_CLASSLOADER);
			if (!IMusicPlayer.class.isAssignableFrom(clazz)) {
				throw new IllegalAccessError("The class " + clazz + " does not implement IMusicPlayer! This should not happen?!");
			}
			player = (IMusicPlayer) clazz.getDeclaredConstructor().newInstance();
			LOGGER.info("Successfully created music player instance");
		} catch (final Exception ex) {
			LOGGER.fatal("Cannot create music player instance. This is a serious bug and the mod will not work. Report to the mod authors", ex);
			System.exit(-1);
		}
	}
	
	public static IMusicPlayer getPlayer() {
		return player;
	}
	
	public static MusicPlayerFiles getFiles() {
		return files;
	}
	
	public static PlaylistManager getPlaylistManager() {
		return playListManager;
	}
	
	public static SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(MusicPlayerManager::setup);
	}
}
