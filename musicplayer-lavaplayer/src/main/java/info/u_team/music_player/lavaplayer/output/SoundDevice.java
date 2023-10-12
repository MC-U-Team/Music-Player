package info.u_team.music_player.lavaplayer.output;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;

public class SoundDevice {
	
	private final long device;
	private final long context;
	
	private final int source;
	
	private final int bufferCount = 8;
	private final IntBuffer buffers;
	
	private final int bufferSize;
	private final ByteBuffer tempBuffer;
	
	private final int sampleRate;
	private final int format;
	
	private final long sleepTime;
	
	public SoundDevice(String name, AudioDataFormat audioDataFormat) throws OpenALException {
		if ("".equals(name)) {
			name = null;
		}
		long foundDevice = ALC11.alcOpenDevice(name);
		if (foundDevice == MemoryUtil.NULL) {
			foundDevice = ALC11.alcOpenDevice((ByteBuffer) null);
		}
		device = foundDevice;
		context = ALC11.alcCreateContext(device, (IntBuffer) null);
		EXTThreadLocalContext.alcSetThreadContext(context);
		AL.createCapabilities(ALC.createCapabilities(device));
		checkErrors();
		
		source = AL11.alGenSources();
		checkErrors();
		
		AL11.alSourcei(source, AL11.AL_LOOPING, AL11.AL_FALSE);
		checkErrors();
		
		buffers = BufferUtils.createIntBuffer(bufferCount);
		AL11.alGenBuffers(buffers);
		checkErrors();
		
		bufferSize = audioDataFormat.totalSampleCount() * 2 * 2;
		tempBuffer = BufferUtils.createByteBuffer(bufferSize);
		
		sampleRate = audioDataFormat.sampleRate;
		format = audioDataFormat.channelCount > 1 ? AL11.AL_FORMAT_STEREO16 : AL11.AL_FORMAT_MONO16;
		
		sleepTime = (long) (((float) bufferSize / 2 / audioDataFormat.channelCount / sampleRate) * 1000);
		
		tempBuffer.clear();
		
		for (int index = 0; index < bufferCount; index++) {
			final int bufferID = buffers.get(index);
			
			AL11.alBufferData(bufferID, format, tempBuffer, sampleRate);
			AL11.alSourceQueueBuffers(source, bufferID);
		}
		AL11.alSourcePlay(source);
		EXTThreadLocalContext.alcSetThreadContext(MemoryUtil.NULL);
	}
	
	public String getName() {
		String name = ALC11.alcGetString(device, ALC11.ALC_ALL_DEVICES_SPECIFIER);
		if (name == null) {
			name = ALC11.alcGetString(device, ALC11.ALC_DEVICE_SPECIFIER);
		}
		if (name == null) {
			name = "Unknown";
		}
		return name;
	}
	
	public void destroy() {
		AL11.alDeleteSources(source);
		AL11.alDeleteBuffers(buffers);
		
		EXTThreadLocalContext.alcSetThreadContext(MemoryUtil.NULL);
		ALC11.alcDestroyContext(context);
		ALC11.alcCloseDevice(device);
	}
	
	public void play(byte[] buffer, int offset, int length) {
		EXTThreadLocalContext.alcSetThreadContext(context);
		while (length > 0) {
			final int written = Math.min(bufferSize, length);
			
			outer: while (true) {
				if (EXTThreadLocalContext.alcGetThreadContext() == MemoryUtil.NULL) {
					return;
				}
				int processedBuffers = AL11.alGetSourcei(source, AL11.AL_BUFFERS_PROCESSED);
				while (processedBuffers-- > 0) {
					final int bufferID = AL11.alSourceUnqueueBuffers(source);
					if (bufferID == AL11.AL_INVALID_VALUE) {
						break;
					}
					
					tempBuffer.clear();
					tempBuffer.put(buffer, offset, written).flip();
					
					AL11.alBufferData(bufferID, format, tempBuffer, sampleRate);
					AL11.alSourceQueueBuffers(source, bufferID);
					
					break outer;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ignored) {
				}
			}
			
			if (AL11.alGetSourcei(source, AL11.AL_SOURCE_STATE) != AL11.AL_PLAYING) {
				AL11.alSourcePlay(source);
			}
			
			length -= written;
			offset += written;
		}
		EXTThreadLocalContext.alcSetThreadContext(MemoryUtil.NULL);
	}
	
	public void checkErrors() throws OpenALException {
		final int error = AL11.alGetError();
		if (error != AL11.AL_NO_ERROR) {
			throw new OpenALException(AL11.alGetString(error));
		}
	}
	
}
