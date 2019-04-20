package info.u_team.music_player.musicplayer;

import java.util.*;

import info.u_team.music_player.lavaplayer.api.*;

public class LoadedTracks {
	
	private String name;
	
	private List<IAudioTrack> tracks;
	
	public LoadedTracks(ISearchResult result) {
		if (result.hasError()) {
			name = result.getErrorMessage();
			tracks = new ArrayList<>();
		} else {
			if (result.isList()) {
				init(result.getTrackList());
			} else {
				init(result.getTrack());
			}
		}
	}
	
	public LoadedTracks(IAudioTrack track) {
		init(track);
	}
	
	public LoadedTracks(IAudioTrackList list) {
		init(list);
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
	
	public String getName() {
		return name;
	}
	
	public List<IAudioTrack> getTracks() {
		return tracks;
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
		LoadedTracks other = (LoadedTracks) obj;
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
