package info.u_team.music_player.musicplayer;

import java.nio.file.Files;
import java.util.*;

import org.apache.commons.io.FileUtils;

public class PlaylistManager {
	
	private final List<Playlist> playlists = new ArrayList<>();
	
	public PlaylistManager() {
	}
	
	public List<Playlist> getPlaylists() {
		return Collections.unmodifiableList(playlists);
	}
	
}
