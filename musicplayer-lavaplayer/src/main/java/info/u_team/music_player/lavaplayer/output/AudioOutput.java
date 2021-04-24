/**
 * Thanks to VSETH-GECO for this amazing audio consumer class for lavaplayer (It is kind of changed) MIT License
 * Copyright (c) 2017 VSETH-GECO Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The
 * above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package info.u_team.music_player.lavaplayer.output;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import info.u_team.music_player.lavaplayer.MusicPlayer;

public class AudioOutput extends Thread {
	
	private final MusicPlayer musicPlayer;
	
	private final AudioFormat format;
	private final DataLine.Info speakerInfo;
	
	private Mixer mixer;
	private SourceDataLine souceLine;
	
	public AudioOutput(MusicPlayer musicPlayer) {
		super("Audio Player");
		this.musicPlayer = musicPlayer;
		format = AudioDataFormatTools.toAudioFormat(musicPlayer.getAudioDataFormat());
		speakerInfo = new DataLine.Info(SourceDataLine.class, format);
		setMixer("");
	}
	
	@Override
	public void run() {
		try {
			final AudioPlayer player = musicPlayer.getAudioPlayer();
			final AudioDataFormat dataformat = musicPlayer.getAudioDataFormat();
			
			final AudioInputStream stream = AudioPlayerInputStream.createStream(player, dataformat, dataformat.frameDuration(), false);
			
			final byte[] buffer = new byte[dataformat.chunkSampleCount * dataformat.channelCount * 2];
			final long frameDuration = dataformat.frameDuration();
			int chunkSize;
			while (true) {
				if (souceLine == null || !souceLine.isOpen()) {
					closeLine();
					if (!createLine()) {
						sleep(500);
						continue;
					}
				}
				if (!player.isPaused()) {
					if ((chunkSize = stream.read(buffer)) >= 0) {
						souceLine.write(buffer, 0, chunkSize);
						if (musicPlayer.getOutputConsumer() != null) {
							musicPlayer.getOutputConsumer().accept(Arrays.copyOf(buffer, buffer.length), chunkSize);
						}
					} else {
						throw new IllegalStateException("Audiostream ended. This should not happen.");
					}
				} else {
					souceLine.drain();
					sleep(frameDuration);
				}
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setMixer(String name) {
		if (mixer != null && mixer.getMixerInfo().getName().equals(name)) {
			return;
		}
		final Mixer oldMixer = mixer;
		mixer = findMixer(name, speakerInfo);
		closeLine();
		if (oldMixer != null) {
			if (!hasLinesOpen(oldMixer)) {
				oldMixer.close();
			}
		}
	}
	
	public String getMixer() {
		if (mixer == null) {
			return null;
		}
		return mixer.getMixerInfo().getName();
	}
	
	public DataLine.Info getSpeakerInfo() {
		return speakerInfo;
	}
	
	private boolean createLine() {
		if (mixer != null) {
			try {
				final SourceDataLine line = (SourceDataLine) mixer.getLine(speakerInfo);
				final AudioDataFormat dataFormat = musicPlayer.getAudioDataFormat();
				line.open(format, dataFormat.chunkSampleCount * dataFormat.channelCount * 2 * 5);
				line.start();
				souceLine = line;
				return true;
			} catch (final LineUnavailableException ex) {
			}
		}
		return false;
	}
	
	private void closeLine() {
		if (souceLine != null) {
			souceLine.flush();
			souceLine.stop();
			souceLine.close();
		}
	}
	
	private Mixer findMixer(String name, Line.Info lineInfo) {
		Mixer defaultMixer = null;
		for (final Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			final Mixer mixer = AudioSystem.getMixer(mixerInfo);
			if (mixer.isLineSupported(lineInfo)) {
				if (mixerInfo.getName().equals(name)) {
					return mixer;
				}
				if (defaultMixer == null) {
					defaultMixer = mixer;
				}
			}
		}
		return defaultMixer;
	}
	
	public static boolean hasLinesOpen(Mixer mixer) {
		return mixer.getSourceLines().length != 0 || mixer.getTargetLines().length != 0;
	}
}
