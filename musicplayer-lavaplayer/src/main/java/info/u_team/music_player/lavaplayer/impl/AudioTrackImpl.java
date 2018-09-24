package info.u_team.music_player.lavaplayer.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import info.u_team.music_player.lavaplayer.api.*;

public class AudioTrackImpl implements IAudioTrack {
	
	private AudioTrack track;
	
	public AudioTrackImpl(AudioTrack track) {
		this.track = track;
	}
	
	@Override
	public IAudioTrackInfo getInfo() {
		return new AudioTrackInfoImpl(track.getInfo());
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
