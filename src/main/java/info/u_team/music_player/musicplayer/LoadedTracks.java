package info.u_team.music_player.musicplayer;

import java.util.*;

import info.u_team.music_player.lavaplayer.api.*;

public class LoadedTracks {
	
	private final String uri;
	
	private String name;
	
	private List<IAudioTrack> tracks;
	
	private final boolean error;
	
	public LoadedTracks(ISearchResult result) {
		uri = result.getUri();
		if (result.hasError()) {
			name = result.getErrorMessage();
			tracks = new ArrayList<>();
			error = true;
		} else {
			error = false;
			if (result.isList()) {
				init(result.getTrackList());
			} else {
				init(result.getTrack());
			}
		}
	}
	
	public LoadedTracks(IAudioTrack track) {
		init(track);
		uri = track.getInfo().getURI();
		error = false;
	}
	
	public LoadedTracks(IAudioTrackList list) {
		init(list);
		uri = list.hasUri() ? list.getUri() : "No Uri";
		error = false;
	}
	
	private void init(IAudioTrack track) {
		name = track.getInfo().getTitle();
		tracks = new ArrayList<>();
		tracks.add(track);
	}
	
	private void init(IAudioTrackList list) {
		name = list.getName();
		tracks = new ArrayList<>(list.getTracks());
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getName() {
		return name;
	}
	
	public List<IAudioTrack> getTracks() {
		return tracks;
	}
	
	public boolean hasError() {
		return error;
	}
	
	public int size() {
		return tracks.size();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (error ? 1231 : 1237);
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
		LoadedTracks other = (LoadedTracks) obj;
		if (error != other.error)
			return false;
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
