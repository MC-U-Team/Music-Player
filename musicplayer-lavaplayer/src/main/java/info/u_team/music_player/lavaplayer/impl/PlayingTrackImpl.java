package info.u_team.music_player.lavaplayer.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.audio.IPlayingTrack;

public class PlayingTrackImpl extends AudioTrackImpl implements IPlayingTrack {
	
	private final IMusicPlayer musicPlayer;
	
	public PlayingTrackImpl(IMusicPlayer musicPlayer, AudioTrack track) {
		super(track);
		this.musicPlayer = musicPlayer;
	}
	
	@Override
	public IAudioTrack getOriginalTrack() {
		return track.getUserData(IAudioTrack.class);
	}
	
	@Override
	public long getPosition() {
		return (long) (super.getPosition() * musicPlayer.getSpeed());
	}
	
	@Override
	public void setPosition(long position) {
		super.setPosition((long) (position * musicPlayer.getSpeed()));
	}
	
}
