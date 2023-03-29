package info.u_team.music_player.init;

import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.musicplayer.MusicPlayerInitManager;

public class MusicPlayerClientConstruct {
	
	public static void construct() {
		System.setProperty("http.agent", "Chrome");
		
		ClientConfig.load();
		
		DependencyManager.load();
		
		MusicPlayerInitManager.register();
		MusicPlayerKeys.register();
		
		MusicPlayerEventHandler.register();
	}
	
}
