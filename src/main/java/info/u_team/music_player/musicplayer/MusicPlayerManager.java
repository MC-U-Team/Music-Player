package info.u_team.music_player.musicplayer;

import org.apache.logging.log4j.*;

import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.lavaplayer.api.*;

public class MusicPlayerManager {
	
	private static final Logger logger = LogManager.getLogger();
	
	public static IMusicPlayer player;
	
	public static void construct() {
		logger.info(":::::::::::::::::::::::::::::::::::::");
		generatePlayer();
		System.out.println(player);
		
		player.startAudioOutput();
		
		player.registerEventHandler(new IMusicPlayerEvents() {
			
			public void onPlay(IAudioTrack track) {
				System.out.println(track);
			}
			
			public void onStop() {
				System.out.println("STOP");
			}
			
			public void onSearchSuccess(State state) {
				System.out.println("SUCCESS");
			}
			
			public void onSearchFailed(String error, Exception exeption) {
				System.out.println(error);
			}
		});
		
		player.getTrackSearch().play("E-Rotic - King Kong");
		player.setVolume(5);
	}
	
	private static void generatePlayer() {
		try {
			Class<?> clazz = Class.forName("info.u_team.music_player.lavaplayer.MusicPlayer", true, DependencyManager.classloader);
			if (!IMusicPlayer.class.isAssignableFrom(clazz)) {
				throw new IllegalAccessError("The class " + clazz + " does not implement IMusicPlayer! This should not happen?!");
			}
			player = (IMusicPlayer) clazz.newInstance();
			logger.info("Successfully created music player instance.");
		} catch (Exception ex) {
			logger.fatal("Cannot create music player instance. This is a serious bug and the mod will not work. Report to the mod authors.", ex);
			System.exit(0);
		}
	}
}
