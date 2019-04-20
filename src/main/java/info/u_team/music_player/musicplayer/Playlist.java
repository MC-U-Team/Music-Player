package info.u_team.music_player.musicplayer;

import java.util.*;

import info.u_team.music_player.lavaplayer.api.*;

public class Playlist {
	
	private String name;
	
	private final Set<String> tracks;
	
	private transient boolean loaded;
	private final transient Map<String, LoadedTracks> loadedTracks;
	
	public Playlist(String name) {
		this.name = name;
		tracks = new HashSet<>();
		loaded = false;
		loadedTracks = new HashMap<>();
	}
	
	public void load() {
		if (!loaded) {
			ITrackSearch search = MusicPlayerManager.player.getTrackSearch();
			tracks.forEach(track -> search.getTracks(track, LoadedTracks::new));
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
	
	public boolean add(IAudioTrack track) {
		String uri = track.getInfo().getURI();
		loadedTracks.put(uri, new LoadedTracks(track));
		return tracks.add(uri);
	}
	
	public boolean add(IAudioTrackList tracklist) {
		if (!tracklist.isSearch() && tracklist.hasUri()) {
			String uri = tracklist.getUri();
			loadedTracks.put(uri, new LoadedTracks(tracklist));
			return tracks.add(uri);
		}
		return false;
	}
	
	public boolean remove(String uri) {
		loadedTracks.remove(uri);
		return tracks.remove(uri);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tracks == null) ? 0 : tracks.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Playlist other = (Playlist) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tracks == null) {
			if (other.tracks != null)
				return false;
		} else if (!tracks.equals(other.tracks))
			return false;
		return true;
	}
	
}
