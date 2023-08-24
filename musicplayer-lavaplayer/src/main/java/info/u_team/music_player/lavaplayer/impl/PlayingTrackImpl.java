package info.u_team.music_player.lavaplayer.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import info.u_team.music_player.lavaplayer.MusicPlayer;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.audio.IPlayingTrack;

public class PlayingTrackImpl extends AudioTrackImpl implements IPlayingTrack {
	
	private final MusicPlayer musicPlayer;
	
	public PlayingTrackImpl(MusicPlayer musicPlayer, AudioTrack track) {
		super(track);
		this.musicPlayer = musicPlayer;
	}
	
	@Override
	public IAudioTrack getOriginalTrack() {
		return track.getUserData(IAudioTrack.class);
	}
	
	@Override
	public long getPosition() {
		final long position;
		if (track.isSeekable()) {
			position = musicPlayer.getCurrentTrackPosition();
		} else {
			position = super.getPosition();
		}
		return Math.max(0, Math.min(super.getDuration(), position));
	}
	
	@Override
	public void setPosition(long position) {
		if (track.isSeekable()) {
			position = Math.max(0, Math.min(super.getDuration(), position));
			super.setPosition(position);
			musicPlayer.setCurrentTrackPosition(position);
		}
	}
}
