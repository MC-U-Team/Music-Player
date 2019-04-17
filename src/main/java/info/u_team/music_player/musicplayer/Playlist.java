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

	public void add(IAudioTrack track) {
		String uri = track.getInfo().getURI();
		tracks.add(uri);
//		loadedTracks.add(new LoadedTracks(uri, Arrays.asList(track)));
	}

	public void add(IAudioTrackList tracklist) {
		if (!tracklist.isSearch() && tracklist.hasUri()) {
			String uri = tracklist.getUri();
			tracks.add(uri);
//			loadedTracks.add(new LoadedTracks(uri, tracklist.getTracks()));
		}
	}

	public void remove(IAudioTrack track) {
		tracks.remove(track.getInfo().getURI());
		loadedTracks.remove(track);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
