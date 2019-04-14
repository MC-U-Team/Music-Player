package info.u_team.music_player.musicplayer;

import java.util.*;

import info.u_team.music_player.lavaplayer.api.IAudioTrack;

public class Playlist {
	
	private final List<IAudioTrack> loadedTracks;
	
	public Playlist() {
		loadedTracks = new ArrayList<>();
	}
	
}
