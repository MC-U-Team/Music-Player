package info.u_team.music_player.lavaplayer.queue;

import java.util.*;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.*;

import info.u_team.music_player.lavaplayer.MusicPlayer;
import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.lavaplayer.impl.AudioTrackImpl;

public class TrackScheduler extends AudioEventAdapter implements ITrackScheduler {
	
	private AudioPlayer audioplayer;
	
	private PlayList playlist;
	
	private boolean repeat, shuffle;
	
	public TrackScheduler(AudioPlayer audioplayer) {
		this.audioplayer = audioplayer;
		this.playlist = new PlayList();
		this.repeat = false;
		this.shuffle = false;
	}
	
	public void play(AudioTrack track) {
		if (track == null) {
			stop();
			return;
		}
		MusicPlayer.getEventHandler().forEach(event -> event.onPlay(new AudioTrackImpl(track)));
		audioplayer.playTrack(track);
	}
	
	public boolean isPlaying() {
		return audioplayer.getPlayingTrack() != null;
	}
	
	private void queue(AudioTrack track, boolean first) {
		if (!isPlaying()) {
			play(track);
		} else {
			if (first) {
				playlist.offerFirst(track);
			} else {
				playlist.offerLast(track);
			}
		}
	}
	
	public void queueFirst(AudioTrack track) {
		queue(track, true);
	}
	
	public void queueLast(AudioTrack track) {
		queue(track, false);
	}
	
	public void stop() {
		MusicPlayer.getEventHandler().forEach(event -> event.onStop());
		playlist.clear();
		audioplayer.stopTrack();
	}
	
	public void skip() {
		AudioTrack track = playlist.pollFirst();
		if (track == null) {
			stop();
		} else {
			play(track);
		}
	}
	
	public void shuffle() {
		AudioTrack track = playlist.pollRandom();
		if (track == null) {
			stop();
		} else {
			play(track);
		}
	}
	
	public void mix() {
		playlist.mix();
	}
	
	public List<AudioTrack> getQueueImpl() {
		return playlist.getTracks();
	}
	
	public AudioTrack getCurrentTrackImpl() {
		return audioplayer.getPlayingTrack();
	}
	
	@Override
	public List<IAudioTrack> getQueue() {
		ArrayList<IAudioTrack> list = new ArrayList<>();
		playlist.getTracks().forEach(track -> list.add(new AudioTrackImpl(track)));
		return list;
	}
	
	@Override
	public IAudioTrack getCurrentTrack() {
		return new AudioTrackImpl(audioplayer.getPlayingTrack());
	}
	
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}
	
	public boolean isRepeat() {
		return repeat;
	}
	
	public boolean isShuffle() {
		return shuffle;
	}
	
	public void setPaused(boolean pause) {
		audioplayer.setPaused(pause);
	}
	
	public boolean isPaused() {
		return audioplayer.isPaused();
	}
	
	@Override
	public void onTrackEnd(AudioPlayer audioplayer, AudioTrack track, AudioTrackEndReason reason) {
		switch (reason) {
		case FINISHED:
		case LOAD_FAILED:
			if (repeat) {
				play(track.makeClone());
			} else if (shuffle) {
				shuffle();
			} else {
				skip();
			}
			break;
		case STOPPED:
			stop();
			break;
		default:
			break;
		}
	}
}
