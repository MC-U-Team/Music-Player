package info.u_team.music_player.json;

import java.util.List;

public class PlaylistSerialization {
	
	private final String name;
	private final List<String> uris;
	
	public PlaylistSerialization(String name, List<String> uris) {
		this.name = name;
		this.uris = uris;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getUris() {
		return uris;
	}
	
}
