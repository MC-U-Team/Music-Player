package info.u_team.music_player.musicplayer;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.util.WrappedObject;

public class Playlist {
	
	private String name;
	
	private final Set<WrappedObject<String>> uris;
	
	private transient boolean loaded;
	private final transient Map<WrappedObject<String>, LoadedTracks> loadedTracks;
	
	private transient boolean playing;
	
	private transient Queue<LoadedTracks> loadQueue;
	
	public Playlist(String name) {
		this.name = name;
		uris = new LinkedHashSet<>();
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
			uris.forEach(uri -> search.getTracks(uri.get(), result -> {
				LoadedTracks loadedTrack = new LoadedTracks(uri, result);
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
		WrappedObject<String> uri = new WrappedObject<>(track.getInfo().getURI());
		loadedTracks.put(uri, new LoadedTracks(uri, track));
		return uris.add(uri);
	}
	
	public boolean add(IAudioTrackList trackList) {
		if (!trackList.isSearch() && trackList.hasUri()) {
			WrappedObject<String> uri = new WrappedObject<>(trackList.getUri());
			loadedTracks.put(uri, new LoadedTracks(uri, trackList));
			return uris.add(uri);
		}
		return false;
	}
	
	public boolean remove(WrappedObject<String> uri) {
		loadedTracks.remove(uri);
		return uris.remove(uri);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getTrackSize() {
		return uris.size();
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
	
}
