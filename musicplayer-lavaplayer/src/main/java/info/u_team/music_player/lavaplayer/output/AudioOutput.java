package info.u_team.music_player.lavaplayer.output;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioInputStream;

import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import info.u_team.music_player.lavaplayer.MusicPlayer;

public class AudioOutput extends Thread {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AudioOutput.class);
	
	private final MusicPlayer musicPlayer;
	
	private SoundDevice device;
	
	public AudioOutput(MusicPlayer musicPlayer) {
		super("Audio Player");
		this.musicPlayer = musicPlayer;
		setAudioDevice("");
	}
	
	@Override
	public void run() {
		try {
			final AudioPlayer player = musicPlayer.getAudioPlayer();
			final AudioDataFormat dataformat = musicPlayer.getAudioDataFormat();
			final long frameDuration = dataformat.frameDuration();
			
			final AudioInputStream stream = AudioPlayerInputStream.createStream(player, dataformat, frameDuration, false);
			final byte[] buffer = new byte[dataformat.totalSampleCount()];
			int chunkSize;
			
			while (true) {
				if (!player.isPaused()) {
					if ((chunkSize = stream.read(buffer)) >= 0) {
						device.play(buffer, 0, chunkSize);
						if (musicPlayer.getOutputConsumer() != null) {
							musicPlayer.getOutputConsumer().accept(Arrays.copyOf(buffer, buffer.length), chunkSize);
						}
					}
				} else {
					sleep(frameDuration * 5);
				}
			}
		} catch (final Exception ex) {
			LOGGER.error("Cannot play music with open al", ex);
		}
	}
	
	public List<String> audioDevices() {
		final List<String> list = ALUtil.getStringList(0, ALC11.ALC_ALL_DEVICES_SPECIFIER);
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}
	
	public synchronized void setAudioDevice(String name) {
		if (device != null) {
			device.destroy();
		}
		try {
			device = new SoundDevice(name, musicPlayer.getAudioDataFormat());
		} catch (OpenALException ex) {
			LOGGER.error("Cannot open sound device for music player", ex);
		}
	}
	
	public String getAudioDevice() {
		if (device == null) {
			return null;
		}
		return device.getName();
	}
	
}
