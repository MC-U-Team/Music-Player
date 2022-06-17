package info.u_team.music_player.lavaplayer.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.audio.IPlayingTrack;

public class PlayingTrackImpl extends AudioTrackImpl implements IPlayingTrack {
	
	public PlayingTrackImpl(AudioTrack track) {
		super(track);
	}
	
	@Override
	public IAudioTrack getOriginalTrack() {
		return track.getUserData(IAudioTrack.class);
	}
	
}
