package info.u_team.music_player.musicplayer;

import org.apache.logging.log4j.*;

import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;

public class MusicPlayerManager {
	
	private static final Logger logger = LogManager.getLogger();
	
	public static IMusicPlayer player;
	
	public static final PlaylistManager playlistmanager = new PlaylistManager();
	
	public static void construct() {
		generatePlayer();
		player.startAudioOutput();
		playlistmanager.loadFromFile();
		System.out.println("_______________________________________________________________________________________________");
		System.out.println(playlistmanager.getPlaylists());
		Playlists playlists = playlistmanager.getPlaylists();
		
		Playlist play1 = new Playlist("Test liste");
		player.getTrackSearch().getTracks("https://www.youtube.com/watch?v=L6mLEVNvDMU", result -> result.getTrack());
		player.getTrackSearch().getTracks("https://www.youtube.com/watch?v=GVPM63TAeLg", result -> result.getTrack());
		
		playlists.add(play1);
		
		Playlist play2 = new Playlist("ROFLLMAO");
		player.getTrackSearch().getTracks("https://www.youtube.com/watch?v=HXBwmNkKfZo", result -> result.getTrack());
		player.getTrackSearch().getTracks("https://www.youtube.com/watch?v=QsvLBp43r10", result -> result.getTrack());
		
		playlists.add(play2);
		
		playlistmanager.writeToFile();
	}
	
	private static void generatePlayer() {
		try {
			Class<?> clazz = Class.forName("info.u_team.music_player.lavaplayer.MusicPlayer", true, DependencyManager.musicplayerclassloader);
			if (!IMusicPlayer.class.isAssignableFrom(clazz)) {
				throw new IllegalAccessError("The class " + clazz + " does not implement IMusicPlayer! This should not happen?!");
			}
			player = (IMusicPlayer) clazz.newInstance();
			logger.info("Successfully created music player instance");
		} catch (Exception ex) {
			logger.fatal("Cannot create music player instance. This is a serious bug and the mod will not work. Report to the mod authors", ex);
			System.exit(0);
		}
	}
}
