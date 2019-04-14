package info.u_team.music_player.musicplayer;

import java.util.*;

import info.u_team.music_player.lavaplayer.api.IAudioTrack;

public class Playlist {
	
	private final List<String> tracks;
	
	private transient boolean loaded;
	private final transient List<IAudioTrack> loadedTracks;
	
	public Playlist(Collection<String> unloadedTracks) {
		this.tracks = new ArrayList<>(unloadedTracks);
		loaded = false;
		loadedTracks = new ArrayList<>();
	}
	
	public void load() {
		if (!loaded) {
			tracks.forEach((track) -> {
				MusicPlayerManager.player.getTrackSearch().getTracks(track, result -> {
					loadedTracks.addAll(result.getTracks());
				});
			});
			loaded = true;
		}
	}
	
	public void unload() {
		if (loaded) {
			loadedTracks.clear();
			loaded = false;
		}
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public void add(IAudioTrack track) {
		tracks.add(track.getInfo().getURI());
		loadedTracks.add(track);
	}
	
}
