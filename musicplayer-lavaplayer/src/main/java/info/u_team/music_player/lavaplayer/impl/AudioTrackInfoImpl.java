package info.u_team.music_player.lavaplayer.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import info.u_team.music_player.lavaplayer.api.IAudioTrackInfo;

public class AudioTrackInfoImpl implements IAudioTrackInfo {
	
	private final AudioTrackInfo info;
	
	public AudioTrackInfoImpl(AudioTrackInfo info) {
		this.info = info;
	}
	
	@Override
	public String getTitle() {
		return info.title;
	}
	
	@Override
	public String getAuthor() {
		return info.author;
	}
	
	@Override
	public String getIdentifier() {
		return info.identifier;
	}
	
	@Override
	public String getURI() {
		return info.uri;
	}
	
	@Override
	public boolean isStream() {
		return info.isStream;
	}
	
}
