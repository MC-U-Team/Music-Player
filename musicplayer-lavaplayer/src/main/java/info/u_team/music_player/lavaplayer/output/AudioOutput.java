/**
 * Thanks to VSETH-GECO for this amazing audio consumer class for lavaplayer (It is kind of changed)
 * 
 * MIT License
 * 
 * Copyright (c) 2017 VSETH-GECO
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package info.u_team.music_player.lavaplayer.output;

import javax.sound.sampled.*;

import com.sedmelluq.discord.lavaplayer.format.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import info.u_team.music_player.lavaplayer.MusicPlayer;

public class AudioOutput extends Thread {
	
	private final MusicPlayer musicPlayer;
	
	public AudioOutput(MusicPlayer musicPlayer) {
		super("Audio Player");
		this.musicPlayer = musicPlayer;
	}
	
	@Override
	public void run() {
		try {
			AudioPlayer player = musicPlayer.getAudioPlayer();
			AudioDataFormat dataformat = musicPlayer.getAudioDataFormat();
			
			AudioInputStream stream = AudioPlayerInputStream.createStream(player, dataformat, dataformat.frameDuration(), true);
			
			SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat());
			SourceDataLine output = (SourceDataLine) AudioSystem.getLine(info);
			
			int buffersize = dataformat.chunkSampleCount * dataformat.channelCount * 2;
			
			output.open(stream.getFormat(), buffersize * 5);
			output.start();
			
			byte[] buffer = new byte[buffersize];
			int chunkSize;
			long frameDuration = dataformat.frameDuration();
			while (true) {
				if (!player.isPaused()) {
					if ((chunkSize = stream.read(buffer)) >= 0) {
						output.write(buffer, 0, chunkSize);
					} else {
						throw new IllegalStateException("Audiostream ended. This should not happen.");
					}
				} else {
					output.drain();
					sleep(frameDuration);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
