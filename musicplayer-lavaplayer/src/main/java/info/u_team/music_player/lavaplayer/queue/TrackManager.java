package info.u_team.music_player.lavaplayer.queue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import info.u_team.music_player.lavaplayer.api.audio.IPlayingTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.lavaplayer.api.queue.ITrackQueue;
import info.u_team.music_player.lavaplayer.impl.PlayingTrackImpl;

public class TrackManager extends AudioEventAdapter implements ITrackManager {
	
	private final AudioPlayer audioPlayer;
	
	private TrackQueueWrapper queueWrapper;
	
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
		queueWrapper = null;
	}
	
	@Override
	public void setTrackQueue(ITrackQueue queue) {
		if (queue == null) {
			queueWrapper = null;
		}
		queueWrapper = new TrackQueueWrapper(queue);
	}
	
	@Override
	public void skip() {
		if (queueWrapper == null) {
			stop();
			return;
		}
		if (queueWrapper.calculateNext() && queueWrapper.getNext() != null) {
			audioPlayer.startTrack(queueWrapper.getNext(), false);
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
	public IPlayingTrack getCurrentTrack() {
		return audioPlayer.getPlayingTrack() == null ? null : new PlayingTrackImpl(audioPlayer.getPlayingTrack());
	}
}
