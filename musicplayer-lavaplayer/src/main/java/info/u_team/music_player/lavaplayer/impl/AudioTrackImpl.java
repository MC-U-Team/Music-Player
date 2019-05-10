package info.u_team.music_player.lavaplayer.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import info.u_team.music_player.lavaplayer.api.audio.*;

public class AudioTrackImpl implements IAudioTrack {
	
	private final AudioTrack track;
	
	private final IAudioTrackInfo info;
	
	public AudioTrackImpl(AudioTrack track) {
		this.track = track;
		info = track.getInfo() != null ? new AudioTrackInfoImpl(track.getInfo()) : null;
	}
	
	@Override
	public IAudioTrackInfo getInfo() {
		return info;
	}
	
	@Override
	public long getPosition() {
		return track.getPosition();
	}
	
	@Override
	public void setPosition(long position) {
		track.setPosition(position);
	}
	
	@Override
	public long getDuration() {
		return track.getDuration();
	}
	
}
