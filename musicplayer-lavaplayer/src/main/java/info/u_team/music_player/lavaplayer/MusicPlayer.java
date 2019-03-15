package info.u_team.music_player.lavaplayer;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.sedmelluq.discord.lavaplayer.format.*;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration.ResamplingQuality;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.lavaplayer.output.AudioOutput;
import info.u_team.music_player.lavaplayer.queue.TrackScheduler;
import info.u_team.music_player.lavaplayer.search.*;

public class MusicPlayer implements IMusicPlayer {
	
	private static final ConcurrentLinkedQueue<IMusicPlayerEvents> eventhandler = new ConcurrentLinkedQueue<>();
	
	private final AudioPlayerManager audioplayermanager;
	private final AudioDataFormat audiodataformat;
	private final AudioPlayer audioplayer;
	private final AudioOutput audiooutput;
	
	private final TrackScheduler trackscheduler;
	private final TrackSearch tracksearch;
	private final TrackDirectSearch trackdirectsearch;
	
	public MusicPlayer() {
		audioplayermanager = new DefaultAudioPlayerManager();
		audiodataformat = new Pcm16AudioDataFormat(2, 48000, 960, true);
		audioplayer = audioplayermanager.createPlayer();
		audiooutput = new AudioOutput(this);
		
		trackscheduler = new TrackScheduler(audioplayer);
		tracksearch = new TrackSearch(audioplayermanager, trackscheduler);
		trackdirectsearch = new TrackDirectSearch(audioplayermanager, trackscheduler);
		
		setup();
	}
	
	private void setup() {
		audioplayermanager.setFrameBufferDuration(1000);
		audioplayermanager.setPlayerCleanupThreshold(Long.MAX_VALUE);
		
		audioplayermanager.getConfiguration().setResamplingQuality(ResamplingQuality.HIGH);
		audioplayermanager.getConfiguration().setOpusEncodingQuality(10);
		audioplayermanager.getConfiguration().setOutputFormat(audiodataformat);
		
		AudioSourceManagers.registerRemoteSources(audioplayermanager);
		AudioSourceManagers.registerLocalSource(audioplayermanager);
		
		audioplayer.addListener(trackscheduler);
	}
	
	public AudioPlayerManager getAudioPlayerManager() {
		return audioplayermanager;
	}
	
	public AudioDataFormat getAudioDataFormat() {
		return audiodataformat;
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioplayer;
	}
	
	@Override
	public TrackScheduler getTrackScheduler() {
		return trackscheduler;
	}
	
	@Override
	public TrackSearch getTrackSearch() {
		return tracksearch;
	}
	
	@Override
	public TrackDirectSearch getTrackDirectSearch() {
		return trackdirectsearch;
	}
	
	@Override
	public void startAudioOutput() {
		audiooutput.start();
	}
	
	@Override
	public void setVolume(int volume) {
		audioplayer.setVolume(volume);
	}
	
	@Override
	public int getVolume() {
		return audioplayer.getVolume();
	}
	
	@Override
	public void registerEventHandler(IMusicPlayerEvents events) {
		eventhandler.add(events);
	}
	
	@Override
	public void unregisterEventHandler(IMusicPlayerEvents events) {
		eventhandler.remove(events);
	}
	
	public static ConcurrentLinkedQueue<IMusicPlayerEvents> getEventHandler() {
		return eventhandler;
	}
}
