package info.u_team.music_player.lavaplayer.queue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.*;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.*;
import info.u_team.music_player.lavaplayer.impl.AudioTrackImpl;

public class TrackManager extends AudioEventAdapter implements ITrackManager {
	
	private final AudioPlayer audioPlayer;
	
	private TrackQueueWrapper queue;
	
	public TrackManager(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
		audioPlayer.addListener(this);
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason == AudioTrackEndReason.FINISHED || endReason == AudioTrackEndReason.LOAD_FAILED) {
			skip();
		}
	}
	
	@Override
	public void start() {
		setPaused(false);
		skip();
	}
	
	@Override
	public void stop() {
		setPaused(false);
		audioPlayer.stopTrack();
		queue = null;
	}
	
	@Override
	public void setTrackQueue(ITrackQueue queue) {
		this.queue = new TrackQueueWrapper(queue);
	}
	
	@Override
	public void skip() {
		if (queue == null) {
			stop();
			return;
		}
		if (queue.calculateNext() && queue.getNext() != null) {
			audioPlayer.startTrack(queue.getNext(), false);
		} else {
			stop();
		}
	}
	
	@Override
	public void setPaused(boolean value) {
		audioPlayer.setPaused(value);
	}
	
	@Override
	public boolean isPaused() {
		return audioPlayer.isPaused();
	}
	
	@Override
	public IAudioTrack getCurrentTrack() {
		return audioPlayer.getPlayingTrack() == null ? null : new AudioTrackImpl(audioPlayer.getPlayingTrack());
	}
}
