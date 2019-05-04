package info.u_team.music_player.musicplayer;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.common.collect.LinkedHashMultimap;

import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.util.WrappedObject;

public class Playlist {
	
	public String name;
	
	public final List<WrappedObject<String>> uris;
	
	// Do not serialize the following fields
	private transient boolean loaded;
	private transient final Map<WrappedObject<String>, LoadedTracks> loadedTracks;
	
	private transient boolean playing;
	
	private transient final Queue<LoadedTracks> loadQueue;
	
	/**
	 * Should never be used. Only used for Gson deserialization to init these
	 * transient fields. <a href=
	 * "https://github.com/google/gson/issues/364">https://github.com/google/gson/issues/364</a>
	 */
	Playlist() {
		uris = new ArrayList<>();
		loadedTracks = new LinkedHashMap<>();
		loadQueue = new ConcurrentLinkedQueue<>();
	}
	
	public Playlist(String name) {
		this.name = name;
		uris = new ArrayList<>();
		loadedTracks = new LinkedHashMap<>();
		loadQueue = new ConcurrentLinkedQueue<>();
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
	
	public WrappedObject<String> add(IAudioTrack track) {
		WrappedObject<String> uri = new WrappedObject<>(track.getInfo().getURI());
		loadedTracks.put(uri, new LoadedTracks(uri, track));
		uris.add(uri);
		return uri;
	}
	
	public WrappedObject<String> add(IAudioTrackList trackList) {
		if (!trackList.isSearch() && trackList.hasUri()) {
			WrappedObject<String> uri = new WrappedObject<>(trackList.getUri());
			loadedTracks.put(uri, new LoadedTracks(uri, trackList));
			uris.add(uri);
			return uri;
		}
		return null;
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
	
	public Collection<LoadedTracks> getLoadedTracks() {
		return Collections.unmodifiableCollection(loadedTracks.values());
	}
	
	/**
	 * Move the uri in the list up or down.
	 * 
	 * @param uri
	 *            - value for up, + for down
	 * @param value
	 * 
	 * @return If move was successful
	 */
	public boolean move(WrappedObject<String> uri, int value) {
		if (!loaded) {
			return false;
		}
		int oldIndex = uris.indexOf(uri);
		int newIndex = oldIndex + value;
		if (newIndex >= 0 && newIndex < uris.size()) {
			uris.remove(oldIndex);
			uris.add(newIndex, uri);
			
			LinkedHashMultimap<String, String> map = LinkedHashMultimap.create();
			map.put
			
			return true;
			
		} else {
			return false;
		}
	}
	
}
