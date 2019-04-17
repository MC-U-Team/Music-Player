package info.u_team.music_player.lavaplayer.impl;

import java.util.*;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

import info.u_team.music_player.lavaplayer.api.*;

public class AudioTrackListImpl implements IAudioTrackList {

	private final String uri;
	private final AudioPlaylist playlist;

	public AudioTrackListImpl(AudioPlaylist playlist) {
		this(null, playlist);
	}

	public AudioTrackListImpl(String uri, AudioPlaylist playlist) {
		this.uri = uri;
		this.playlist = playlist;
	}

	@Override
	public String getName() {
		return playlist.getName();
	}

	@Override
	public List<IAudioTrack> getTracks() {
		ArrayList<IAudioTrack> list = new ArrayList<>();
		playlist.getTracks().forEach(track -> list.add(new AudioTrackImpl(track)));
		return list;
	}

	@Override
	public IAudioTrack getSelectedTrack() {
		return playlist.getSelectedTrack() != null ? new AudioTrackImpl(playlist.getSelectedTrack()) : null;
	}

	@Override
	public boolean isSearch() {
		return playlist.isSearchResult();
	}

	@Override
	public boolean hasUri() {
		return uri != null && !uri.isEmpty();
	}

	@Override
	public String getUri() {
		return uri;
	}

}
