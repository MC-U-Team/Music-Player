package info.u_team.music_player.lavaplayer.testimpl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenALTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger("TEST");
	
	public static void main(String[] args) {
		final long device = ALC10.alcOpenDevice((String) null);
		if (checkALCError(device, "Open device")) {
			return;
		}
		final ALCCapabilities alccapabilities = ALC.createCapabilities(device);
		final long context = ALC10.alcCreateContext(device, (IntBuffer) null);
		ALC10.alcMakeContextCurrent(context);
		
		final ALCapabilities alcapabilities = AL.createCapabilities(alccapabilities);
		checkALError("Initialization");
		
		{
			final int source = AL10.alGenSources();
			final int[] buffers = new int[1];
			AL11.alGenBuffers(buffers);
			
			AL10.alSourcePlay(source);
			
			ByteBuffer byteBuf = MemoryUtil.memAlloc(50000);
			
			while (true) {
				AL11.alBufferData(buffers[0], AL10.AL_FORMAT_STEREO16, byteBuf, 48000);
				if (checkALError("CHECK")) {
					throw new IllegalStateException();
				}
				AL11.alSourcef(source, AL11.AL_GAIN, 1F);
				AL11.alSourcei(source, AL11.AL_BUFFER, buffers[0]);
				AL11.alSourcePlay(source);
				
				// System.out.println("ROFL");
				//
				// AL11.alBufferData(buffers[0], AL11.AL_FORMAT_STEREO16, new short[512], 48000);
				//
				// if (checkALError("CHECK")) {
				// throw new IllegalStateException();
				// }
				//
				// // AL11.alSourceQueueBuffers(source, buffers[0]);
				// AL11.alSourcei(source, AL11.AL_BUFFER, buffers[0]);
			}
		}
	}
	
	private static String alcErrorToString(int pErrorCode) {
		switch (pErrorCode) {
		case 40961:
			return "Invalid device.";
		case 40962:
			return "Invalid context.";
		case 40963:
			return "Illegal enum.";
		case 40964:
			return "Invalid value.";
		case 40965:
			return "Unable to allocate memory.";
		default:
			return "An unrecognized error occurred.";
		}
	}
	
	static boolean checkALCError(long pDeviceHandle, String pOperationState) {
		int i = ALC10.alcGetError(pDeviceHandle);
		if (i != 0) {
			LOGGER.error("{}{}: {}", pOperationState, pDeviceHandle, alcErrorToString(i));
			return true;
		} else {
			return false;
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
			LOGGER.error("{}: {}", pOperationState, alErrorToString(i));
			return true;
		} else {
			return false;
		}
	}
}
