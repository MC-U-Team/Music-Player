package info.u_team.music_player.lavaplayer;

import com.sedmelluq.discord.lavaplayer.format.*;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration.ResamplingQuality;

import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.lavaplayer.api.search.ITrackSearch;
import info.u_team.music_player.lavaplayer.output.AudioOutput;
import info.u_team.music_player.lavaplayer.queue.TrackManager;
import info.u_team.music_player.lavaplayer.search.TrackSearch;
import info.u_team.music_player.lavaplayer.sources.AudioSources;

public class MusicPlayer implements IMusicPlayer {
	
	private final AudioPlayerManager audioPlayerManager;
	private final AudioDataFormat audioDataFormat;
	private final AudioPlayer audioPlazer;
	private final AudioOutput audioOutput;
	
	private final TrackSearch trackSearch;
	private final TrackManager trackManager;
	
	public MusicPlayer() {
		audioPlayerManager = new DefaultAudioPlayerManager();
		audioDataFormat = new Pcm16AudioDataFormat(2, 48000, 960, true);
		audioPlazer = audioPlayerManager.createPlayer();
		audioOutput = new AudioOutput(this);
		
		trackSearch = new TrackSearch(audioPlayerManager);
		trackManager = new TrackManager(audioPlazer);
		
		setup();
	}
	
	private void setup() {
		audioPlayerManager.setFrameBufferDuration(1000);
		audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
		
		audioPlayerManager.getConfiguration().setResamplingQuality(ResamplingQuality.HIGH);
		audioPlayerManager.getConfiguration().setOpusEncodingQuality(10);
		audioPlayerManager.getConfiguration().setOutputFormat(audioDataFormat);
		
		AudioSources.registerSources(audioPlayerManager);
	}
	
	public AudioPlayerManager getAudioPlayerManager() {
		return audioPlayerManager;
	}
	
	public AudioDataFormat getAudioDataFormat() {
		return audioDataFormat;
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioPlazer;
	}
	
	@Override
	public ITrackManager getTrackManager() {
		return trackManager;
	}
	
	@Override
	public ITrackSearch getTrackSearch() {
		return trackSearch;
	}
	
	@Override
	public void startAudioOutput() {
		audioOutput.start();
	}
	
	@Override
	public void setVolume(int volume) {
		audioPlazer.setVolume(volume);
	}
	
	@Override
	public int getVolume() {
		return audioPlazer.getVolume();
	}
}
