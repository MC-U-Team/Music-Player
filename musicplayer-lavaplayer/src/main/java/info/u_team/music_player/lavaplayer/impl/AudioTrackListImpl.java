package info.u_team.music_player.lavaplayer.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

import info.u_team.music_player.lavaplayer.api.*;

public class AudioTrackListImpl implements IAudioTrackList {
	
	private final String uri;
	private final AudioPlaylist playlist;
	
	private final List<IAudioTrack> tracks;
	
	private final IAudioTrack selectedTrack;
	
	public AudioTrackListImpl(AudioPlaylist playlist) {
		this(null, playlist);
	}
	
	public AudioTrackListImpl(String uri, AudioPlaylist playlist) {
		this.uri = uri;
		this.playlist = playlist;
		tracks = playlist.getTracks().stream().filter(track -> track != null).map(AudioTrackImpl::new).collect(Collectors.toList());
		selectedTrack = playlist.getSelectedTrack() != null ? new AudioTrackImpl(playlist.getSelectedTrack()) : null;
	}
	
	@Override
	public String getName() {
		return playlist.getName();
	}
	
	@Override
	public List<IAudioTrack> getTracks() {
		return tracks;
	}
	
	@Override
	public IAudioTrack getSelectedTrack() {
		return selectedTrack;
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
