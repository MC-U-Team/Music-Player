package info.u_team.music_player.lavaplayer.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrackList;

public class AudioTrackListImpl implements IAudioTrackList {
	
	private final String uri;
	private final AudioPlaylist playList;
	
	private final List<IAudioTrack> tracks;
	
	private final IAudioTrack selectedTrack;
	
	public AudioTrackListImpl(AudioPlaylist playlist) {
		this(null, playlist);
	}
	
	public AudioTrackListImpl(String uri, AudioPlaylist playList) {
		this.uri = uri;
		this.playList = playList;
		tracks = playList.getTracks().stream().filter(track -> track != null).map(AudioTrackImpl::new).collect(Collectors.toList());
		selectedTrack = playList.getSelectedTrack() != null ? new AudioTrackImpl(playList.getSelectedTrack()) : null;
	}
	
	@Override
	public String getName() {
		return playList.getName();
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
		return playList.isSearchResult();
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
