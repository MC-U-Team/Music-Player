package info.u_team.music_player.lavaplayer.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrackInfo;

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
	
	@Override
	public String getFixedTitle() {
		if (info.title == null || info.title.equals("Unknown title")) {
			return info.uri;
		}
		return info.title;
	}
	
	@Override
	public String getFixedAuthor() {
		if (info.author == null || info.author.equals("Unknown artist")) {
			return "";
		}
		return info.author;
	}
	
}
