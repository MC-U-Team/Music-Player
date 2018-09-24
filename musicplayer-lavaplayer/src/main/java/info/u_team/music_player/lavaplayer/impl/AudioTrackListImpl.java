package info.u_team.music_player.lavaplayer.impl;

import java.util.*;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

import info.u_team.music_player.lavaplayer.api.*;

public class AudioTrackListImpl implements IAudioTrackList {
	
	private AudioPlaylist playlist;
	
	public AudioTrackListImpl(AudioPlaylist playlist) {
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
		return new AudioTrackImpl(playlist.getSelectedTrack());
	}
	
}
