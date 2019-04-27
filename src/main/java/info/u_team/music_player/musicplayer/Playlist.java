package info.u_team.music_player.musicplayer;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import info.u_team.music_player.lavaplayer.api.*;

public class Playlist {
	
	private String name;
	
	private final Set<String> tracks;
	
	private transient boolean loaded;
	private final transient Map<String, LoadedTracks> loadedTracks;
	
	private transient boolean playing;
	
	private transient Queue<LoadedTracks> loadQueue;
	
	public Playlist(String name) {
		this.name = name;
		tracks = new HashSet<>();
		loaded = false;
		loadedTracks = new HashMap<>();
		loadQueue = new LinkedBlockingQueue<>();
	}
	
	public void load() {
		if (loaded) {
			return;
		}
		MusicPlayerManager.getExecutor().execute(() -> {
			// Just to be sure the list is clear so we don't load tracks twice which were
			// not removed, because of threaded loading. Also clear load queue
			unload();
			ITrackSearch search = MusicPlayerManager.getPlayer().getTrackSearch();
			tracks.forEach(uri -> search.getTracks(uri, result -> {
				LoadedTracks loadedTrack = new LoadedTracks(result);
				loadedTracks.put(uri, loadedTrack);
				loadQueue.offer(loadedTrack); // Add all loaded elements to queue so we can use add them async to our gui list
			}));
			loaded = true;
		});
	}
	
	public void unload() {
		loadedTracks.clear();
		loaded = false;
		loadQueue.clear();
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
	
	public int getTrackSize() {
		return tracks.size();
	}
	
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public Queue<LoadedTracks> getLoadQueue() {
		return loadQueue;
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
