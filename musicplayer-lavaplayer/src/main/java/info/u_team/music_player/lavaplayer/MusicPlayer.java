package info.u_team.music_player.lavaplayer;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.sound.sampled.DataLine.Info;

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration.ResamplingQuality;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AllocatingAudioFrameBuffer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.lavaplayer.api.output.IOutputConsumer;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.lavaplayer.api.search.ITrackSearch;
import info.u_team.music_player.lavaplayer.output.AudioOutput;
import info.u_team.music_player.lavaplayer.queue.TrackManager;
import info.u_team.music_player.lavaplayer.search.TrackSearch;
import info.u_team.music_player.lavaplayer.sources.AudioSources;
import info.u_team.music_player.lavaplayer.util.ObservableValue;

public class MusicPlayer implements IMusicPlayer {
	
	private final AudioPlayerManager audioPlayerManager;
	private final AudioDataFormat audioDataFormat;
	private final AudioPlayer audioPlayer;
	private final AudioOutput audioOutput;
	
	private final TrackSearch trackSearch;
	private final TrackManager trackManager;
	
	private IOutputConsumer outputConsumer;
	
	private final ObservableValue<Float> speed;
	
	private long currentTrackPosition;
	
	public MusicPlayer() {
		audioPlayerManager = new DefaultAudioPlayerManager();
		audioDataFormat = new Pcm16AudioDataFormat(2, 48000, 960, true);
		audioPlayer = audioPlayerManager.createPlayer();
		audioOutput = new AudioOutput(this);
		
		trackSearch = new TrackSearch(audioPlayerManager);
		trackManager = new TrackManager(this, audioPlayer);
		
		speed = new ObservableValue<>(1F);
		
		setup();
	}
	
	private void setup() {
		audioPlayerManager.setFrameBufferDuration(1000);
		audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
		
		audioPlayerManager.getConfiguration().setResamplingQuality(ResamplingQuality.HIGH);
		audioPlayerManager.getConfiguration().setOpusEncodingQuality(10);
		audioPlayerManager.getConfiguration().setOutputFormat(audioDataFormat);
		
		AudioSources.registerSources(audioPlayerManager);
		
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		
		audioPlayer.addListener(new AudioEventAdapter() {
			
			@Override
			public void onTrackStart(AudioPlayer player, AudioTrack track) {
				currentTrackPosition = track.getPosition();
			}
		});
		
		audioPlayerManager.getConfiguration().setFrameBufferFactory((bufferDuration, format, stopping) -> new AllocatingAudioFrameBuffer(bufferDuration, format, stopping) {
			
			@Override
			public AudioFrame provide() {
				return updateTrackPosition(super.provide());
			}
			
			@Override
			public AudioFrame provide(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
				return updateTrackPosition(super.provide(timeout, unit));
			}
			
			private AudioFrame updateTrackPosition(AudioFrame frame) {
				if (frame != null && !frame.isTerminator()) {
					currentTrackPosition += frame.getFormat().frameDuration() * speed.getValue();
				}
				return frame;
			}
		});
		
		speed.registerListener(value -> {
			audioPlayer.setFilterFactory((track, format, output) -> {
				final TimescalePcmAudioFilter filter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
				filter.setSpeed(value);
				return Collections.singletonList(filter);
			});
		});
	}
	
	public AudioPlayerManager getAudioPlayerManager() {
		return audioPlayerManager;
	}
	
	public AudioDataFormat getAudioDataFormat() {
		return audioDataFormat;
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
	
	public IOutputConsumer getOutputConsumer() {
		return outputConsumer;
	}
	
	public long getCurrentTrackPosition() {
		return currentTrackPosition;
	}
	
	public void setCurrentTrackPosition(long currentTrackPosition) {
		this.currentTrackPosition = currentTrackPosition;
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
	public void setMixer(String name) {
		audioOutput.setMixer(name);
	}
	
	@Override
	public String getMixer() {
		return audioOutput.getMixer();
	}
	
	@Override
	public Info getSpeakerInfo() {
		return audioOutput.getSpeakerInfo();
	}
	
	@Override
	public void setVolume(int volume) {
		audioPlayer.setVolume(volume);
	}
	
	@Override
	public int getVolume() {
		return audioPlayer.getVolume();
	}
	
	@Override
	public void setSpeed(float speed) {
		this.speed.setValue(speed);
	}
	
	@Override
	public float getSpeed() {
		return speed.getValue();
	}
	
	@Override
	public void setOutputConsumer(IOutputConsumer consumer) {
		outputConsumer = consumer;
	}
}
