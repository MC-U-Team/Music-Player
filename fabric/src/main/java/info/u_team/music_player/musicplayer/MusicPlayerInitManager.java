package info.u_team.music_player.musicplayer;

import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.dependency.DependencyManager;

public class MusicPlayerInitManager {
	
	public static void register() {
		MusicPlayerManager.setup(DependencyManager.MUSICPLAYER_CLASSLOADER, ClientConfig.getInstance().internalPlaylists);
	}
	
}
