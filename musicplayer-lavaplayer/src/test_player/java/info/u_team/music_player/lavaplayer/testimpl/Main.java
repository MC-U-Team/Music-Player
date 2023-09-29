package info.u_team.music_player.lavaplayer.testimpl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.u_team.music_player.lavaplayer.MusicPlayer;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.lavaplayer.api.queue.ITrackQueue;

public class Main {
	
	private final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		final MusicPlayer musicPlayer = new MusicPlayer();
		
		{
			long device = ALC11.alcOpenDevice((String) null);
			long context = ALC11.alcCreateContext(device, (IntBuffer) null);
			ALCCapabilities deviceCaps = ALC.createCapabilities(device);
			ALC11.alcMakeContextCurrent(context);
			AL.createCapabilities(deviceCaps);
			
			System.out.println("Speaker: " + device);
			System.out.println("Context: " + context);
			
			int bufferId = AL11.alGenBuffers();
			int sourceId = AL11.alGenSources();
			
			AL11.alSourcePlay(sourceId);
			if (checkALError("PLAY")) {
				throw new IllegalStateException();
			}
			
			if (checkALError("CREATOR")) {
				throw new IllegalStateException();
			}
			
			ByteBuffer byteBuf = MemoryUtil.memAlloc(3840);
			byteBuf.order(ByteOrder.BIG_ENDIAN);
			
			musicPlayer.setOutputConsumer((buffer, chunkSize) -> {
				EXTThreadLocalContext.alcSetThreadContext(context);
				
				// endianConverter(buffer, 4);
				System.out.println(buffer.length + " -> " + chunkSize);
				
				byteBuf.clear();
				byteBuf.put(buffer, 0, chunkSize);
				// byteBuf.position(0);
				// byteBuf.limit(chunkSize);
				System.out.println("REMM: " + byteBuf.remaining());
				
				System.out.println(byteBuf.toString());
				AL11.alBufferData(bufferId, AL10.AL_FORMAT_STEREO16, byteBuf.asShortBuffer(), 48000);
				if (checkALError("BUFFER")) {
					// throw new IllegalStateException();
				}
				AL11.alSourcef(sourceId, AL11.AL_GAIN, 0.5F);
				if (checkALError("GAIN")) {
					// throw new IllegalStateException();
				}
				AL10.alSourceQueueBuffers(sourceId, bufferId);
				if (checkALError("SOURCE BUFFER")) {
					// throw new IllegalStateException();
				}
				
				// Check if the source has finished playing buffers (to prevent overfilling)
				int processedBuffers = AL10.alGetSourcei(sourceId, AL10.AL_BUFFERS_PROCESSED);
				if (processedBuffers > 0) {
				}
				
				System.out.println(AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE));
				
				EXTThreadLocalContext.alcSetThreadContext(0L);
			});
		}
		
		musicPlayer.startAudioOutput();
		
		musicPlayer.setVolume(10);
		
		final ITrackManager manager = musicPlayer.getTrackManager();
		
		musicPlayer.getTrackSearch().getTracks("https://www.youtube.com/watch?v=bnoAe_5i__8", result -> {
			if (result.isList()) {
				manager.setTrackQueue(new TrackQueue(result.getTrackList().getTracks()));
			} else {
				manager.setTrackQueue(new TrackQueue(Arrays.asList(result.getTrack())));
			}
			log.info("Loaded all tracks -> starting now");
			manager.start();
		});
		
		new Thread(() -> {
			final Scanner scanner = new Scanner(System.in);
			while (scanner.hasNext()) {
				final String line = scanner.nextLine();
				if (line.startsWith("pause")) {
					manager.setPaused(true);
				} else if (line.startsWith("unpause")) {
					manager.setPaused(false);
				} else if (line.startsWith("skip")) {
					manager.skip();
				} else if (line.startsWith("rate")) {
					final IAudioTrack audioTrack = manager.getCurrentTrack();
					System.out.println((float) audioTrack.getPosition() / audioTrack.getDuration());
				} else if (line.startsWith("speed")) {
					try {
						musicPlayer.setSpeed(Float.parseFloat(line.substring("speed ".length())));
					} catch (NumberFormatException ex) {
					}
				} else if (line.startsWith("pitch")) {
					try {
						musicPlayer.setPitch(Float.parseFloat(line.substring("pitch ".length())));
					} catch (NumberFormatException ex) {
					}
				} else if (line.startsWith("stop")) {
					manager.stop();
					System.exit(0);
				} else if (line.startsWith("setending")) {
					final IAudioTrack audioTrack = manager.getCurrentTrack();
					audioTrack.setPosition(audioTrack.getDuration() - 10000);
				} else if (line.startsWith("playing")) {
					final IAudioTrack audioTrack = manager.getCurrentTrack();
					System.out.println(audioTrack.getInfo().getAuthor() + " - " + audioTrack.getInfo().getTitle());
				}
			}
			scanner.close();
		}, "Music player manager").start();
		
		final AtomicInteger counter = new AtomicInteger();
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			final IAudioTrack audioTrack = manager.getCurrentTrack();
			if (audioTrack != null) {
				System.out.println(counter.getAndIncrement() + ": " + formatter.format(Instant.now()) + " -> Rate: " + String.format("%.2f%%", ((float) (audioTrack.getPosition()) / audioTrack.getDuration()) * 100));
			}
		}, 1, 10, TimeUnit.SECONDS);
	}
	
	private class TrackQueue implements ITrackQueue {
		
		private final Random random = new Random();
		
		private final List<IAudioTrack> tracks;
		
		public TrackQueue(List<IAudioTrack> tracks) {
			System.out.println("Init track queue with " + tracks.size() + " tracks.");
			this.tracks = tracks;
		}
		
		@Override
		public boolean calculateNext() {
			return true;
		}
		
		@Override
		public IAudioTrack getNext() {
			return tracks.get(random.nextInt(tracks.size()));
		}
		
	}
	
	public static void endianConverter(byte[] buffer, int length) {
		if (buffer.length % length != 0 || length % 2 != 0) {
			throw new IllegalStateException();
		}
		for (int index = 0; index < buffer.length; index += length) {
			for (int endianIndex = 0; endianIndex < length / 2; endianIndex++) {
				final byte temp = buffer[index + endianIndex];
				buffer[index + endianIndex] = buffer[index + length - endianIndex - 1];
				buffer[index + length - endianIndex - 1] = temp;
			}
		}
	}
	
	private static String alErrorToString(int pErrorCode) {
		switch (pErrorCode) {
		case 40961:
			return "Invalid name parameter.";
		case 40962:
			return "Invalid enumerated parameter value.";
		case 40963:
			return "Invalid parameter parameter value.";
		case 40964:
			return "Invalid operation.";
		case 40965:
			return "Unable to allocate memory.";
		default:
			return "An unrecognized error occurred.";
		}
	}
	
	static boolean checkALError(String pOperationState) {
		int i = AL10.alGetError();
		if (i != 0) {
			LoggerFactory.getLogger(Main.class).error("{}: {}", pOperationState, alErrorToString(i));
			return true;
		} else {
			return false;
		}
	}
	
}
