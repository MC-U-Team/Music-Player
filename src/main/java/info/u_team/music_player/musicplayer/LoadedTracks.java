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
			}
		}
	}

	public boolean hasName() {
		return name != null;
	}

	public String getName() {
		return name;
	}

	public List<IAudioTrack> getTracks() {
		return tracks;
	}

}
